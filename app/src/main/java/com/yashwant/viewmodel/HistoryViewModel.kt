package com.yashwant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(SettingsManager(application), application)


    private val _orderList = MutableStateFlow<List<OrderItem>>(emptyList())

    // Explicitly batayein ki ye StateFlow<List<OrderItem>> hai
    val orderList: StateFlow<List<OrderItem>> = _orderList.asStateFlow()

    init {
        fetchOrderHistory()
    }

    private fun fetchOrderHistory() {
        viewModelScope.launch {
            // Hum UserRepository mein 'getOrdersStream' function banayenge
            repository.getOrdersStream().collect { orders ->
                _orderList.value = orders
            }
        }
    }
}