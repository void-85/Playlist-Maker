package com.example.playlistmaker.di


import com.example.playlistmaker.data.repositories.AppPrefsRepositoryImpl
import com.example.playlistmaker.data.repositories.AudioRepositoryImpl
import com.example.playlistmaker.data.repositories.FavTracksRepositoryImpl
import org.koin.dsl.module

import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.repositories.AppPrefsRepository
import com.example.playlistmaker.domain.api.repositories.AudioRepository
import com.example.playlistmaker.domain.api.repositories.TracksRepository
import com.example.playlistmaker.domain.db.FavTracksRepository


val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<AppPrefsRepository> {
        AppPrefsRepositoryImpl(get(), get())
    }

    single<AudioRepository> {
        AudioRepositoryImpl()
    }

    single<FavTracksRepository> {
        FavTracksRepositoryImpl(get())
    }
}