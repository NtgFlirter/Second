package com.yashwant.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.yashwant.data.PrefManager
import com.yashwant.model.ProfileState

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val prefManager = PrefManager(application)

    var state = mutableStateOf(prefManager.loadProfile())
        private set

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