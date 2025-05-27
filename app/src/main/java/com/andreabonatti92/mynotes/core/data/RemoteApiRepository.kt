package com.andreabonatti92.mynotes.core.data

import com.andreabonatti92.mynotes.auth.data.model.AccessTokenData
import com.andreabonatti92.mynotes.auth.data.model.ErrorResponse
import com.andreabonatti92.mynotes.auth.data.model.LoginRequest
import com.andreabonatti92.mynotes.auth.data.model.LoginResponse
import com.andreabonatti92.mynotes.auth.data.model.RefreshRequest
import com.andreabonatti92.mynotes.auth.data.model.RegisterRequest
import com.andreabonatti92.mynotes.auth.data.model.TokenData
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

    override suspend fun login(email: String, password: String): Result<TokenData> {
        return try {
            val response: HttpResponse = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }

            if (response.status.isSuccess()) {
                // Deserialize body into LoginResponse
                val loginResponse: LoginResponse = response.body()
                Result.success(loginResponse.data)
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

    override suspend fun getNotes(): Result<List<Note>> {
        return try {
            val token = tokenProvider.getAccessToken()
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

    override suspend fun refresh(): Result<AccessTokenData> {
        return try {
            val token = tokenProvider.getRefreshToken()
            val response = client.post("$baseUrl/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequest(token))
            }

            if (response.status.isSuccess()) {
                // Deserialize body into String
                val body: AccessTokenData = response.body()
                Result.success(body)
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
}