package com.ideabs.mynotes.core.data

interface ApiRepository {
    suspend fun register(email: String, password: String): Result<Unit>

    suspend fun login(email: String, password: String): Result<RemoteApiRepository.TokenData>
}