package com.ideabs.mynotes.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ideabs.mynotes.core.data.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val result = apiRepository.login(email, password)

            _loginState.value = result.fold(
                onSuccess = { token ->
                    LoginState.Success(
                        accessToken = token.accessToken,
                        refreshToken = token.refreshToken,
                        tokenType = token.tokenType
                    )
                },
                onFailure = {
                    LoginState.Error(it.message ?: "Unknown Error")
                }
            )
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(
        val accessToken: String, val refreshToken: String, val tokenType: String
    ) : LoginState()

    data class Error(val message: String) : LoginState()
}