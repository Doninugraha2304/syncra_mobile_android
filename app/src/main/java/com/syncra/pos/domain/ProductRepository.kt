package com.syncra.pos.domain

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    // Raw Materials
    fun getAllRawMaterials(): Flow<List<RawMaterial>>
    suspend fun insertRawMaterial(name: String, stockQuantity: Double, unit: String, costPerUnit: Double)
    suspend fun updateRawMaterial(id: Long, name: String, stockQuantity: Double, unit: String, costPerUnit: Double)
    suspend fun deleteRawMaterial(id: Long)
    suspend fun updateRawMaterialStock(id: Long, newStock: Double)

    // Products
    fun getAllProductsWithRecipes(): Flow<List<Product>>
    suspend fun createProductWithRecipe(name: String, price: Double, barcode: String?, recipe: List<Pair<Long, Double>>)
    suspend fun updateProduct(id: Long, name: String, price: Double, barcode: String?)
    suspend fun deleteProduct(id: Long)
    
    // POS Transaction
    suspend fun sellProduct(productId: Long, quantity: Int)
    
    // Transactions History
    suspend fun insertTransaction(transaction: Transaction)
    fun getAllTransactions(): Flow<List<Transaction>>
}
