package com.example

import android.app.Application
import com.example.kolsa.di.koinAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(koinAppModules)
        }
    }
}
