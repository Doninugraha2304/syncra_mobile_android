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

    fun updateRawMaterial(id: Long, name: String, stockQuantity: Double, unit: String, costPerUnit: Double) {
        viewModelScope.launch {
            repository.updateRawMaterial(id, name, stockQuantity, unit, costPerUnit)
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

    fun updateProduct(id: Long, name: String, price: Double, barcode: String?) {
        viewModelScope.launch {
            repository.updateProduct(id, name, price, barcode)
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
            if (_cart.value.isEmpty()) return@launch
            
            val timestamp = System.currentTimeMillis()
            var totalAmount = 0.0
            val transactionItems = mutableListOf<com.syncra.pos.domain.TransactionItem>()
            
            _cart.value.forEach { (product, quantity) ->
                repository.sellProduct(product.id, quantity)
                totalAmount += product.price * quantity
                
                transactionItems.add(
                    com.syncra.pos.domain.TransactionItem(
                        productId = product.id,
                        productName = product.name,
                        quantity = quantity,
                        price = product.price
                    )
                )
            }
            
            val transaction = com.syncra.pos.domain.Transaction(
                timestamp = timestamp,
                totalAmount = totalAmount,
                items = transactionItems
            )
            repository.insertTransaction(transaction)
            
            _cart.value = emptyMap() // Clear cart after checkout
        }
    }
}
