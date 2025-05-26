package com.andreabonatti92.mynotes.core.data

import com.andreabonatti92.mynotes.notes.domain.Note

interface ApiRepository {
    suspend fun register(email: String, password: String): Result<Unit>

    suspend fun login(email: String, password: String): Result<RemoteApiRepository.TokenData>

    suspend fun getNotes(): Result<List<Note>>
}