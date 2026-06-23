package com.syncra.pos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syncra.pos.domain.Product
import com.syncra.pos.domain.ProductRepository
import com.syncra.pos.domain.RawMaterial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    val rawMaterials: StateFlow<List<RawMaterial>> = repository.getAllRawMaterials()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _products = repository.getAllProductsWithRecipes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cart = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cart: StateFlow<Map<Product, Int>> = _cart.asStateFlow()

    fun addRawMaterial(name: String, stockQuantity: Double, unit: String, costPerUnit: Double) {
        viewModelScope.launch {
            repository.insertRawMaterial(name, stockQuantity, unit, costPerUnit)
        }
    }

    fun updateRawMaterialStock(id: Long, newStock: Double) {
        viewModelScope.launch {
            repository.updateRawMaterialStock(id, newStock)
        }
    }

    fun deleteRawMaterial(id: Long) {
        viewModelScope.launch {
            repository.deleteRawMaterial(id)
        }
    }

    fun addProductWithRecipe(name: String, price: Double, barcode: String?, recipe: List<Pair<Long, Double>>) {
        viewModelScope.launch {
            repository.createProductWithRecipe(name, price, barcode, recipe)
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProduct(id)
        }
    }

    fun sellProduct(productId: Long, quantity: Int) {
        viewModelScope.launch {
            repository.sellProduct(productId, quantity)
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val currentCart = _cart.value.toMutableMap()
        currentCart[product] = (currentCart[product] ?: 0) + quantity
        _cart.value = currentCart
    }

    fun checkout() {
        viewModelScope.launch {
            _cart.value.forEach { (product, quantity) ->
                repository.sellProduct(product.id, quantity)
            }
            _cart.value = emptyMap() // Clear cart after checkout
        }
    }
}
