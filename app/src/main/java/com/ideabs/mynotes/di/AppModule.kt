package com.ideabs.mynotes.di

import com.ideabs.mynotes.BuildConfig
import com.ideabs.mynotes.auth.presentation.login.LoginViewModel
import com.ideabs.mynotes.auth.presentation.register.RegisterViewModel
import com.ideabs.mynotes.core.data.ApiRepository
import com.ideabs.mynotes.core.data.HttpClientFactory
import com.ideabs.mynotes.core.data.RemoteApiRepository
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { HttpClientFactory.create(CIO.create()) }

    single<ApiRepository> {
        RemoteApiRepository(
            client = get(),
            baseUrl = BuildConfig.BASE_URL
        )
    }

    viewModel { RegisterViewModel(apiRepository = get()) }
    viewModel { LoginViewModel(apiRepository = get()) }
}