package com.ideabs.mynotes.di

import com.ideabs.mynotes.auth.presentation.RegisterViewModel
import com.ideabs.mynotes.core.data.ApiRepository
import com.ideabs.mynotes.core.data.RemoteApiRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
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
}