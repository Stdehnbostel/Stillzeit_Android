package com.example.stillzeit

import android.app.Application
import com.example.stillzeit.data.AppContainer
import com.example.stillzeit.data.DefaultAppContainer

class StillzeitApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}