package com.yashwant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.FoodRepository
import com.yashwant.model.FoodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoodHomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FoodRepository()

    // 1. Asli Data State
    private val _foodList = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodList = _foodList.asStateFlow()

    // 2. Loading State
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadFoodMenu()
    }

    fun getFoodByName(name: String): FoodItem? {
        return foodList.value.find { it.name == name }
    }

    private fun loadFoodMenu() {
        viewModelScope.launch {
            _isLoading.value = true
            val data = repository.getApiMenu()
            _foodList.value = data
            _isLoading.value = false
        }
    }
}