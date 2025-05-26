package com.andreabonatti92.mynotes.core.data

import android.util.Log
import com.andreabonatti92.mynotes.notes.data.dto.NoteDto
import com.andreabonatti92.mynotes.notes.data.mappers.toNote
import com.andreabonatti92.mynotes.notes.domain.Note
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class RemoteApiRepository(
    private val client: HttpClient,
    private val baseUrl: String,
    private val tokenProvider: TokenProvider
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

    override suspend fun login(email: String, password: String): Result<TokenData> {
        return try {
            val response: HttpResponse = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }

            if (response.status.isSuccess()) {
                // Deserialize body into LoginResponse
                val loginResponse: LoginResponse = response.body()
                Result.success(loginResponse.data) // ✅ This is TokenData
            } else {
                val message = parseErrorBody(response.bodyAsText())
                Result.failure(Exception(message))
            }
        } catch (e: ClientRequestException) {
            val message = parseErrorBody(e.response.bodyAsText())
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Serializable
    private data class RegisterRequest(val email: String, val password: String)

    @Serializable
    private data class LoginRequest(val email: String, val password: String)

    @Serializable
    data class LoginResponse(
        val message: String,
        val data: TokenData
    )

    @Serializable
    data class TokenData(
        @SerialName("access_token")
        val accessToken: String,
        @SerialName("refresh_token")
        val refreshToken: String,
        @SerialName("token_type")
        val tokenType: String
    )

    @Serializable
    data class ErrorResponse(val detail: String)

    override suspend fun getNotes(): Result<List<Note>> {
        return try {
            val token = tokenProvider.getAccessToken()
            Log.d("JWT TOKEN", token)
            val response: HttpResponse = client.get("$baseUrl/notes") {
                header(HttpHeaders.Authorization, "Bearer $token")
                accept(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                // Deserialize body into List<Note>
                val body: List<NoteDto> = response.body()
                val notes = body.map { it.toNote() }
                Result.success(notes)
            } else {
                val message = parseErrorBody(response.bodyAsText())
                Result.failure(Exception(message))
            }
        } catch (e: ClientRequestException) {
            val message = parseErrorBody(e.response.bodyAsText())
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}