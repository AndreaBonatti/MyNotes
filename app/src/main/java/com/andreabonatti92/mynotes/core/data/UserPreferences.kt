package com.andreabonatti92.mynotes.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        val EMAIL_KEY = stringPreferencesKey("email")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    suspend fun saveUserPreferences(email: String, accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    val emailFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[EMAIL_KEY]
    }

    val accessTokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    }

    val refreshTokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    suspend fun clearUserPreferences() {
        dataStore.edit { preferences ->
            preferences.remove(EMAIL_KEY)
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }
}