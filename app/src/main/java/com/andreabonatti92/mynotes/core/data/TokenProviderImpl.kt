package com.andreabonatti92.mynotes.core.data

import kotlinx.coroutines.flow.firstOrNull

class TokenProviderImpl(
    private val userPreferences: UserPreferences
) : TokenProvider {

    override suspend fun getAccessToken(): String {
        return userPreferences.accessTokenFlow.firstOrNull()
            ?: throw IllegalStateException("Access token not found!")
    }
}