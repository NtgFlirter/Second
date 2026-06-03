package com.yashwant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepo = FirebaseRepository()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    init {
        // Collect the theme from Firebase Flow
        viewModelScope.launch {
            firebaseRepo.getThemeFlow().collect { updatedTheme ->
                _isDarkTheme.value = updatedTheme
            }
        }
    }

    fun toggleTheme() {
        val newValue = !isDarkTheme.value
        // We update Firestore; the flow in 'init' will detect the change
        // and update the UI automatically.
        firebaseRepo.saveTheme(newValue)
    }
}