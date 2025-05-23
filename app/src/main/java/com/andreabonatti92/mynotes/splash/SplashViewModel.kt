package com.andreabonatti92.mynotes.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreabonatti92.mynotes.core.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Base64

class SplashViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _destination = MutableStateFlow<String?>(null)
    val destination: StateFlow<String?> = _destination

    init {
        viewModelScope.launch {
            val refreshToken = userPreferences.refreshTokenFlow.firstOrNull()

            val nextScreen = if (refreshToken == null || isJwtExpired(refreshToken)) {
                "login"
            } else {
                "home"
            }

            _destination.value = nextScreen
        }
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