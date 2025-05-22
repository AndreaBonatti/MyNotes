package com.ideabs.mynotes.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ideabs.mynotes.core.data.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Base64

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
                onSuccess = { data ->
                    if (
                        data.tokenType.equals("bearer", ignoreCase = true) &&
                        isValidJwt(data.accessToken) &&
                        isValidJwt(data.refreshToken)
                    ) {
                        LoginState.Success(
                            accessToken = data.accessToken,
                            refreshToken = data.refreshToken,
                            tokenType = data.tokenType
                        )
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

    private fun isValidJwt(token: String): Boolean {
        return token.count { it == '.' } == 2 && !isJwtExpired(token)
    }

    private fun isJwtExpired(token: String): Boolean {
        return try {
            val payloadJson = decodeJwtPayload(token)
            val exp = payloadJson["exp"]?.toLongOrNull()
            if (exp != null) {
                val currentTimeSec = System.currentTimeMillis() / 1000
                exp < currentTimeSec // expired if exp is in the past
            } else {
                true // no exp claim? treat as expired
            }
        } catch (e: Exception) {
            true // any parse failure = invalid/expired
        }
    }

    private fun decodeJwtPayload(token: String): Map<String, String> {
        val parts = token.split(".")
        if (parts.size != 3) throw IllegalArgumentException("Invalid JWT structure")

        val payloadBase64 = parts[1]
        val decoded = Base64.getUrlDecoder().decode(payloadBase64)
        val json = String(decoded, Charsets.UTF_8)

        val regex = "\"(\\w+)\":\\s*\"?(\\d+|[^\"]+)\"?".toRegex()
        return regex.findAll(json).associate { matchResult ->
            val (key, value) = matchResult.destructured
            key to value
        }
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