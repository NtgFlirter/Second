package com.yashwant.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.model.ProfileState
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(SettingsManager(application))

    var state = mutableStateOf(ProfileState())
        private set

    init {
        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            repository.getProfileStream().collect { updatedProfile ->
                state.value = updatedProfile
            }
        }
    }

    fun updateState(updated: ProfileState) {
        state.value = updated
    }

    fun saveProfile() {
        viewModelScope.launch {
            repository.saveProfile(state.value)
        }
    }

    //Clears the profile data
    fun clearProfile() {
        viewModelScope.launch {
            val emptyProfile = ProfileState()
            repository.saveProfile(emptyProfile)
            state.value = emptyProfile
        }
    }
}