package com.andreabonatti92.mynotes.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreabonatti92.mynotes.core.data.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading

            val result = apiRepository.register(email, password)

            _registrationState.value = result.fold(
                onSuccess = { RegistrationState.Success },
                onFailure = { RegistrationState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun resetState() {
        _registrationState.value = RegistrationState.Idle
    }
}

sealed class RegistrationState {
    data object Idle : RegistrationState()
    data object Loading : RegistrationState()
    data object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}