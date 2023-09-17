package com.example.playlistmaker


import android.content.Context

import com.example.playlistmaker.data.repositories.AppPrefsRepositoryImpl
import com.example.playlistmaker.data.repositories.AudioRepositoryImpl
import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.data.web.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.AudioRepository
import com.example.playlistmaker.domain.api.Interactor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.useCases.InteractorImpl


object Creator {

    private fun getAppPrefsRepository(context: Context): AppPrefsRepository {
        val appPrefsRepositoryImpl = AppPrefsRepositoryImpl()
        appPrefsRepositoryImpl.init(context)

        return appPrefsRepositoryImpl
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getAudioRepository(): AudioRepository {
        return AudioRepositoryImpl()
    }

    fun provideInteractor(context: Context): Interactor {
        return InteractorImpl(
            getAppPrefsRepository(context),
            getTracksRepository(),
            getAudioRepository()
        )
    }

}