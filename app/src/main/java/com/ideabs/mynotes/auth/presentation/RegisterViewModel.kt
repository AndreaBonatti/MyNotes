package com.ideabs.mynotes.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading

            delay(1000) // // Simulated delay — replace with real registration logic

            if (email == "already@used.com") {
                _registrationState.value = RegistrationState.Error("Email is already in use")
            } else {
                _registrationState.value = RegistrationState.Success
            }
        }
    }

    fun resetState() {
        _registrationState.value = RegistrationState.Idle
    }
}

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}