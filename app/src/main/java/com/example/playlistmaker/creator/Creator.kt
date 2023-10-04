package com.example.playlistmaker.creator


import android.app.Application

import com.example.playlistmaker.data.repositories.AppPrefsRepositoryImpl
import com.example.playlistmaker.data.repositories.AudioRepositoryImpl
import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.data.web.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.AudioRepository
import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.player.MediaInteractorImpl
import com.example.playlistmaker.domain.search.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeInteractorImpl


object Creator {

    private lateinit var application: Application
    fun init(app: Application) {
        application = app
    }


    private fun getAppPrefsRepository(): AppPrefsRepository {
        val appPrefsRepositoryImpl = AppPrefsRepositoryImpl()
        appPrefsRepositoryImpl.init(application)

        return appPrefsRepositoryImpl
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getAudioRepository(): AudioRepository {
        return AudioRepositoryImpl()
    }


    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(
            getAppPrefsRepository()
        )
    }

    fun provideMediaInteractor(): MediaInteractor {
        return MediaInteractorImpl(
            getAppPrefsRepository(),
            getAudioRepository()
        )
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(
            getAppPrefsRepository(),
            getTracksRepository()
        )
    }
}