package com.ideabs.mynotes.core.data

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class RemoteApiRepository(
    private val client: HttpClient,
    private val baseUrl: String
) : ApiRepository {

    override suspend fun register(email: String, password: String): Result<Unit> = runCatching {
        client.post("$baseUrl/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(email, password))
        }
    }

    @Serializable
    private data class RegisterRequest(val email: String, val password: String)
}