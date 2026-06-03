package com.yashwant.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.PrefManager
import com.yashwant.model.ProfileState
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val prefManager = PrefManager()

    var state = mutableStateOf(ProfileState())
        private set

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
        prefManager.saveProfile(state.value)
    }

    fun clearProfile() {
        prefManager.clearProfile()
        state.value = ProfileState()
    }
}
