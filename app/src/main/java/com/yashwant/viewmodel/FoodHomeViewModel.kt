package com.yashwant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.FoodRepository
import com.yashwant.model.FoodItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class FoodHomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FoodRepository(application)

    // 1. Room DB Stream (Observe cached data)
    val foodList: StateFlow<List<FoodItem>> = repository.menuStream
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Track if we are currently refreshing from network
    private val _isRefreshing = MutableStateFlow(true)
    
    // 3. SMART LOADING: Only show loader if local DB is empty AND we are still refreshing
    val isLoading: StateFlow<Boolean> = combine(foodList, _isRefreshing) { list, refreshing ->
        list.isEmpty() && refreshing
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                // Add a hard timeout to ensure the UI is never stuck forever
                withTimeoutOrNull(10000) { 
                    repository.refreshMenu() 
                }
            } catch (e: Exception) {
                // Network errors handled in repository
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun getFoodByName(name: String): FoodItem? {
        val trimmedName = name.trim()
        return foodList.value.find { it.name.trim().equals(trimmedName, ignoreCase = true) }
    }
}