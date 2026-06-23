package com.syncra.pos.domain

data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val barcode: String?
)
