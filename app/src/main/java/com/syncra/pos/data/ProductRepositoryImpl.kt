package com.syncra.pos.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.syncra.pos.domain.Product
import com.syncra.pos.domain.ProductRepository
import com.syncra.pos.domain.RawMaterial
import com.syncra.pos.domain.RecipeIngredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val database: PosDatabase
) : ProductRepository {

    private val queries = database.posDatabaseQueries

    override fun getAllRawMaterials(): Flow<List<RawMaterial>> {
        return queries.getAllRawMaterials()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .combine(kotlinx.coroutines.flow.flowOf(Unit)) { entities, _ ->
                entities.map {
                    RawMaterial(
                        id = it.id,
                        name = it.name,
                        stockQuantity = it.stockQuantity,
                        unit = it.unit,
                        costPerUnit = it.costPerUnit
                    )
                }
            }
    }

    override suspend fun insertRawMaterial(name: String, stockQuantity: Double, unit: String, costPerUnit: Double) {
        withContext(Dispatchers.IO) {
            queries.insertRawMaterial(name, stockQuantity, unit, costPerUnit)
        }
    }

    override suspend fun updateRawMaterial(id: Long, name: String, stockQuantity: Double, unit: String, costPerUnit: Double) {
        withContext(Dispatchers.IO) {
            queries.updateRawMaterial(name, stockQuantity, unit, costPerUnit, id)
        }
    }

    override suspend fun deleteRawMaterial(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteRawMaterial(id)
        }
    }

    override suspend fun updateRawMaterialStock(id: Long, newStock: Double) {
        withContext(Dispatchers.IO) {
            queries.updateRawMaterialStock(newStock, id)
        }
    }

    override fun getAllProductsWithRecipes(): Flow<List<Product>> {
        return queries.getAllProducts().asFlow().mapToList(Dispatchers.IO).map { products ->
            products.map { productEntity ->
                // Fetching recipe synchronously per emission is fine for local SQLite
                val recipeEntities = queries.getRecipeForProduct(productEntity.id).executeAsList()
                val recipe = recipeEntities.map { r ->
                    RecipeIngredient(
                        id = r.id,
                        productId = r.productId,
                        rawMaterialId = r.rawMaterialId,
                        rawMaterialName = r.rawMaterialName,
                        quantityNeeded = r.quantityNeeded,
                        unit = r.unit,
                        costPerUnit = r.costPerUnit,
                        currentStock = r.stockQuantity
                    )
                }
                Product(
                    id = productEntity.id,
                    name = productEntity.name,
                    price = productEntity.price,
                    barcode = productEntity.barcode,
                    recipe = recipe
                )
            }
        }
    }

    override suspend fun createProductWithRecipe(
        name: String,
        price: Double,
        barcode: String?,
        recipe: List<Pair<Long, Double>>
    ) {
        withContext(Dispatchers.IO) {
            database.transaction {
                queries.insertProduct(name, price, barcode)
                val productId = queries.lastInsertRowId().executeAsOne()
                
                recipe.forEach { (rawMaterialId, quantityNeeded) ->
                    queries.insertRecipeIngredient(productId, rawMaterialId, quantityNeeded)
                }
            }
        }
    }

    override suspend fun updateProduct(id: Long, name: String, price: Double, barcode: String?) {
        withContext(Dispatchers.IO) {
            queries.updateProduct(name, price, barcode, id)
        }
    }

    override suspend fun deleteProduct(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteProduct(id)
        }
    }

    override suspend fun sellProduct(productId: Long, quantity: Int) {
        withContext(Dispatchers.IO) {
            database.transaction {
                val recipe = queries.getRecipeForProduct(productId).executeAsList()
                recipe.forEach { ingredient ->
                    val totalDeduction = ingredient.quantityNeeded * quantity
                    val newStock = ingredient.stockQuantity - totalDeduction
                    queries.updateRawMaterialStock(newStock, ingredient.rawMaterialId)
                }
            }
        }
    }
    
    override suspend fun insertTransaction(transaction: com.syncra.pos.domain.Transaction) {
        withContext(Dispatchers.IO) {
            database.transaction {
                queries.insertTransaction(transaction.timestamp, transaction.totalAmount)
                val txId = queries.lastInsertRowId().executeAsOne()
                
                transaction.items.forEach { item ->
                    queries.insertTransactionItem(
                        transactionId = txId,
                        productId = item.productId,
                        productName = item.productName,
                        quantity = item.quantity.toLong(),
                        price = item.price
                    )
                }
            }
        }
    }

    override fun getAllTransactions(): Flow<List<com.syncra.pos.domain.Transaction>> {
        return queries.getAllTransactions().asFlow().mapToList(Dispatchers.IO).map { txEntities ->
            txEntities.map { tx ->
                val items = queries.getItemsForTransaction(tx.id).executeAsList().map { item ->
                    com.syncra.pos.domain.TransactionItem(
                        productId = item.productId,
                        productName = item.productName,
                        quantity = item.quantity.toInt(),
                        price = item.price
                    )
                }
                com.syncra.pos.domain.Transaction(
                    id = tx.id,
                    timestamp = tx.timestamp,
                    totalAmount = tx.totalAmount,
                    items = items
                )
            }
        }
    }
}
