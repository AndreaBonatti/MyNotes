package com.andreabonatti92.mynotes.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreabonatti92.mynotes.core.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val email: StateFlow<String?> = userPreferences.emailFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val accessToken: StateFlow<String?> = userPreferences.accessTokenFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val refreshToken: StateFlow<String?> = userPreferences.refreshTokenFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            userPreferences.clearUserPreferences()
            onLogoutComplete()
        }
    }
}