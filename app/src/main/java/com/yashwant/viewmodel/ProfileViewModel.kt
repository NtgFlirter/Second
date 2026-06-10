package com.yashwant.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashwant.data.SettingsManager
import com.yashwant.data.UserRepository
import com.yashwant.model.AddressState
import com.yashwant.model.ProfileState
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    // UserRepository ko Context ke saath initialize kiya taaki Room/Firebase dono chal sakein
    private val repository = UserRepository(SettingsManager(application), application)

    // 1. Profile State
    var state = mutableStateOf(ProfileState())
        private set

    // 2. Address State (Naya section)
    var addressState = mutableStateOf(AddressState())
        private set

    init {
        // App khulte hi dono cheezein listen/load karo
        observeProfile()
        loadSavedAddress()
    }

    // --- PROFILE LOGIC ---
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

    // --- ADDRESS LOGIC (FOR EDIT ADDRESS SCREEN) ---

    private fun loadSavedAddress() {
        viewModelScope.launch {
            // Hum UserRepository mein loadAddress function banayenge
            val savedAddress = repository.loadAddress()
            addressState.value = savedAddress
        }
    }

    fun updateAddressState(updated: AddressState) {
        addressState.value = updated
    }

    fun saveAddress() {
        viewModelScope.launch {
            repository.saveAddress(addressState.value)
        }
    }

    // Full Reset
    fun clearProfile() {
        viewModelScope.launch {
            val emptyProfile = ProfileState()
            val emptyAddress = AddressState()
            repository.saveProfile(emptyProfile)
            repository.saveAddress(emptyAddress)
            state.value = emptyProfile
            addressState.value = emptyAddress
        }
    }
}