package com.yashwant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(SettingsManager(application), application)

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    init {
        // App khulte hi Cart ko Live listen karo
        viewModelScope.launch {
            repository.getCartStream().collect { items ->
                _cartItems.value = items
            }
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
