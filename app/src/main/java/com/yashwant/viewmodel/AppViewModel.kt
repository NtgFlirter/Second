package com.yashwant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    // Repository connection

    private val repository = UserRepository(SettingsManager(application))

    // Theme ko "Stream" ki tarah observe.
    // stateIn use karne se ye Compose ke liye 'State' ban jata hai
    val isDarkTheme: StateFlow<Boolean> = repository.themeStream.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false // Default Light
    )

    fun toggleTheme() {
        val newValue = !isDarkTheme.value
        viewModelScope.launch {
            repository.toggleTheme(newValue)
        }
    }
}