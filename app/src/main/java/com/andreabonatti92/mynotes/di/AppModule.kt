package com.andreabonatti92.mynotes.di


import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.andreabonatti92.mynotes.BuildConfig
import com.andreabonatti92.mynotes.auth.presentation.login.LoginViewModel
import com.andreabonatti92.mynotes.auth.presentation.register.RegisterViewModel
import com.andreabonatti92.mynotes.core.data.ApiRepository
import com.andreabonatti92.mynotes.core.data.HttpClientFactory
import com.andreabonatti92.mynotes.core.data.RemoteApiRepository
import com.andreabonatti92.mynotes.core.data.UserPreferences
import com.andreabonatti92.mynotes.home.presentation.HomeViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<androidx.datastore.core.DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = {
                androidContext().preferencesDataStoreFile("user_preferences")
            }
        )
    }

    single { UserPreferences(get()) }

    single { HttpClientFactory.create(CIO.create()) }

    single<ApiRepository> {
        RemoteApiRepository(
            client = get(),
            baseUrl = BuildConfig.BASE_URL
        )
    }

    viewModel { RegisterViewModel(apiRepository = get()) }
    viewModel { LoginViewModel(apiRepository = get(), userPreferences = get()) }
    viewModel { HomeViewModel(userPreferences = get()) }
}