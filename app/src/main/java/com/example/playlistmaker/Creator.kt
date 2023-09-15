package com.example.playlistmaker


import android.content.Context

import com.example.playlistmaker.data.level_3_repositories.AppPrefsRepositoryImpl
import com.example.playlistmaker.data.level_3_repositories.AudioRepositoryImpl
import com.example.playlistmaker.data.level_3_repositories.TracksRepositoryImpl
import com.example.playlistmaker.data.level_4_web.RetrofitNetworkClient
import com.example.playlistmaker.domain.level_1_entities.AppPrefsRepository
import com.example.playlistmaker.domain.level_1_entities.AudioRepository
import com.example.playlistmaker.domain.level_1_entities.Interactor
import com.example.playlistmaker.domain.level_1_entities.TracksRepository
import com.example.playlistmaker.domain.level_2_use_cases.InteractorImpl


object Creator {

    private fun getAppPrefsRepository(context: Context): AppPrefsRepository {
        val x = AppPrefsRepositoryImpl()
        x.init(context)
        return x
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getAudioRepository(): AudioRepository {
        return AudioRepositoryImpl()
    }

    fun provideInteractor(context: Context): Interactor {
        return InteractorImpl (
            getAppPrefsRepository(context) ,
            getTracksRepository()          ,
            getAudioRepository()           )
    }

}