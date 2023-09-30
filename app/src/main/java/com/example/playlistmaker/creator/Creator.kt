package com.example.playlistmaker.creator



import android.content.Context

import com.example.playlistmaker.data.repositories.AppPrefsRepositoryImpl
import com.example.playlistmaker.data.repositories.AudioRepositoryImpl
import com.example.playlistmaker.data.repositories.IntentRepositoryImpl
import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.data.web.RetrofitNetworkClient
import com.example.playlistmaker.domain.IntentInteractorImpl
import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.AudioRepository
import com.example.playlistmaker.domain.api.IntentInteractor
import com.example.playlistmaker.domain.api.IntentRepository
import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.player.MediaInteractorImpl
import com.example.playlistmaker.domain.search.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeInteractorImpl



object Creator {

    private fun getAppPrefsRepository(
        context: Context
    ): AppPrefsRepository {
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

    private fun getIntentRepository(
        context: Context
    ): IntentRepository {
        return IntentRepositoryImpl(context)
    }



    fun provideThemeInteractor(
        context: Context,
        setThemeFun: (Boolean) -> Unit,
    ): ThemeInteractor {
        return ThemeInteractorImpl(
            getAppPrefsRepository(context),
            setThemeFun
        )
    }

    fun provideIntentInteractor(
        context: Context
    ): IntentInteractor {
        return IntentInteractorImpl(
            getIntentRepository(context)
        )
    }

    fun provideMediaInteractor(
        context: Context
    ): MediaInteractor {
        return MediaInteractorImpl(
            getAppPrefsRepository(context),
            getAudioRepository()
        )
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(
            getAppPrefsRepository(context),
            getTracksRepository()
        )
    }
}