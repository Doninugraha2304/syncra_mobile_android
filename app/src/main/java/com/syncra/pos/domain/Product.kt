package com.syncra.pos.domain

data class RawMaterial(
    val id: Long,
    val name: String,
    val stockQuantity: Double,
    val unit: String,
    val costPerUnit: Double
)

data class RecipeIngredient(
    val id: Long,
    val productId: Long,
    val rawMaterialId: Long,
    val rawMaterialName: String,
    val quantityNeeded: Double,
    val unit: String,
    val costPerUnit: Double,
    val currentStock: Double
) {
    val totalCost: Double get() = quantityNeeded * costPerUnit
    val possibleStock: Int get() = if (quantityNeeded > 0) (currentStock / quantityNeeded).toInt() else 0
}

data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val barcode: String?,
    val recipe: List<RecipeIngredient> = emptyList()
) {
    // HPP (Harga Pokok Penjualan) is the sum of the cost of all ingredients
    val hpp: Double get() = recipe.sumOf { it.totalCost }
    
    // Max stock available is determined by the most scarce raw material
    val availableStock: Int get() = if (recipe.isEmpty()) 0 else recipe.minOf { it.possibleStock }
}
