package com.andreabonatti92.mynotes

import android.app.Application
import com.andreabonatti92.mynotes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyNotesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyNotesApp)
            androidLogger()

            modules(appModule)
        }
    }
}