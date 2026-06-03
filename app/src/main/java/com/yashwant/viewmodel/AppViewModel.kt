package com.yashwant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yashwant.data.PrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val prefManager = PrefManager(application)

    private val _isDarkTheme =
        MutableStateFlow(prefManager.loadTheme())

    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val newValue = !_isDarkTheme.value
        _isDarkTheme.value = newValue
        prefManager.saveTheme(newValue)
    }
}