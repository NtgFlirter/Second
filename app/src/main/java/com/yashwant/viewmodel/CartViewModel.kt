package com.yashwant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.model.CartItem
import com.yashwant.model.OrderItem // OrderItem model import karein
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(SettingsManager(application), application)

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    // 1. Naya state: Order process ho raha hai ya nahi
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    // Helper: Current User ID expose kiya UI ke liye
    val uid: String? get() = repository.uid

    init {
        // App khulte hi Cart ko Live listen karo
        viewModelScope.launch {
            repository.getCartStream().collect { items ->
                _cartItems.value = items
            }
        }
    }

    // 2. Naya function: Order place karne ke liye
    fun placeOrder(order: OrderItem, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isProcessing.value = true // Loader dikhao (Glitch ko chhupane ke liye)

            val result = repository.placeOrder(order)

            _isProcessing.value = false // Loader hatao
            onResult(result.isSuccess) // UI ko result bhej do
        }
    }

    fun updateQuantity(item: CartItem, newQty: Int) {
        if (newQty > 0) {
            viewModelScope.launch { repository.addToCart(item.copy(quantity = newQty)) }
        } else {
            removeItem(item.name)
        }
    }

    fun removeItem(name: String) {
        viewModelScope.launch { repository.deleteFromCart(name) }
    }
}