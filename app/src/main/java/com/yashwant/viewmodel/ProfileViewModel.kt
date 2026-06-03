package com.yashwant.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.PrefManager
import com.yashwant.model.ProfileState
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Context ki ab zaroorat nahi hai kyunki Firebase Manager use kar rahe hain
    private val prefManager = PrefManager()

    // 2. Shuruat mein default (khali) ProfileState rakhein
    var state = mutableStateOf(ProfileState())
        private set

    // 3. Init block mein cloud se data load karein
    init {
        viewModelScope.launch {
            val cloudProfile = prefManager.loadProfile()
            state.value = cloudProfile
        }
    }

    fun updateState(updated: ProfileState) {
        state.value = updated
    }

    fun saveProfile() {
        // Data Firebase mein save hoga
        prefManager.saveProfile(state.value)
    }

    fun clearProfile() {
        prefManager.clearProfile()
        state.value = ProfileState()
    }
}