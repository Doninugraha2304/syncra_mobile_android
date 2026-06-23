package com.syncra.pos.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.syncra.pos.domain.Product
import com.syncra.pos.domain.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val database: PosDatabase
) : ProductRepository {

    private val queries = database.posDatabaseQueries

    override fun getAllProducts(): Flow<List<Product>> {
        return queries.getAllProducts()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    Product(
                        id = entity.id,
                        name = entity.name,
                        price = entity.price,
                        barcode = entity.barcode
                    )
                }
            }
    }

    override suspend fun insertProduct(name: String, price: Double, barcode: String?) {
        withContext(Dispatchers.IO) {
            queries.insertProduct(name, price, barcode)
        }
    }

    override suspend fun deleteProduct(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteProduct(id)
        }
    }
}
