package com.andreabonatti92.mynotes.core.data

interface TokenProvider {
    suspend fun getAccessToken(): String

    suspend fun getRefreshToken(): String
}