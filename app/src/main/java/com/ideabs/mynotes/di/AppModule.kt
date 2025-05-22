package com.ideabs.mynotes.di

import com.ideabs.mynotes.auth.presentation.login.LoginViewModel
import com.ideabs.mynotes.auth.presentation.register.RegisterViewModel
import com.ideabs.mynotes.core.data.ApiRepository
import com.ideabs.mynotes.core.data.RemoteApiRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

    single<ApiRepository> {
        RemoteApiRepository(
            client = get(),
            baseUrl = "http://192.168.0.56:8000" //TODO insert the api entrypoint
        )
    }

    viewModel { RegisterViewModel(apiRepository = get()) }
    viewModel { LoginViewModel(apiRepository = get()) }
}