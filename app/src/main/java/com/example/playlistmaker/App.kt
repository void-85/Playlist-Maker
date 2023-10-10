package com.example.playlistmaker


import android.app.Application

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModuleModule


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModuleModule)
        }
    }
}