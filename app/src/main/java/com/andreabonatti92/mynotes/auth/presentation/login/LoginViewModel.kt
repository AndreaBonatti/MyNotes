package com.andreabonatti92.mynotes.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreabonatti92.mynotes.core.data.ApiRepository
import com.andreabonatti92.mynotes.core.data.UserPreferences
import com.andreabonatti92.mynotes.core.domain.isValidJwt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiRepository: ApiRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val result = apiRepository.login(email, password)

            _loginState.value = result.fold(
                onSuccess = { data ->
                    if (
                        data.tokenType.equals("bearer", ignoreCase = true) &&
                        isValidJwt(data.accessToken) &&
                        isValidJwt(data.refreshToken)
                    ) {
                        viewModelScope.launch {
                            userPreferences.saveUserPreferences(
                                email, data.accessToken, data.refreshToken
                            )
                        }
                        LoginState.Success
                    } else {
                        LoginState.Error("Invalid token format received from the server")
                    }

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
    data object Success : LoginState()

    data class Error(val message: String) : LoginState()
}