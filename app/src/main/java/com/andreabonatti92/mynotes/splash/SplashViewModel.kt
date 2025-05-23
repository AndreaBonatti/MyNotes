package com.andreabonatti92.mynotes.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreabonatti92.mynotes.core.data.UserPreferences
import com.andreabonatti92.mynotes.core.domain.isValidJwt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _destination = MutableStateFlow<String?>(null)
    val destination: StateFlow<String?> = _destination

    init {
        viewModelScope.launch {
            val refreshToken = userPreferences.refreshTokenFlow.firstOrNull()

            val nextScreen = if (refreshToken == null || !isValidJwt(refreshToken)) {
                "login"
            } else {
                "home"
            }

            _destination.value = nextScreen
        }
    }
}