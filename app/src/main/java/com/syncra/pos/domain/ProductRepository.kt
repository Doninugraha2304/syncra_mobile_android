package com.syncra.pos.domain

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun insertProduct(name: String, price: Double, barcode: String?)
    suspend fun deleteProduct(id: Long)
}
