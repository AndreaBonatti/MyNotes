package com.ideabs.mynotes.core.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class RemoteApiRepository(
    private val client: HttpClient,
    private val baseUrl: String
) : ApiRepository {

    override suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            val response = client.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(email, password))
            }

            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val message = parseErrorBody(response.bodyAsText())
                Result.failure(Exception(message))
            }
        } catch (e: ClientRequestException) {
            val message = parseErrorBody(e.response.bodyAsText())
            Result.failure(Exception(message))
        } catch (e: Exception) {
            // Network error, 5xx, etc.
            Result.failure(e)
        }
    }

    private fun parseErrorBody(body: String): String {
        return try {
            val error = Json.decodeFromString<ErrorResponse>(body)
            error.detail
        } catch (ex: Exception) {
            "Invalid request"
        }
    }

    @Serializable
    private data class RegisterRequest(val email: String, val password: String)

    @Serializable
    data class ErrorResponse(val detail: String)
}