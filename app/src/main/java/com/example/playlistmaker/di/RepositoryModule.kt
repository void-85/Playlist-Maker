package com.example.playlistmaker.di


import com.example.playlistmaker.data.repositories.AppPrefsRepositoryImpl
import com.example.playlistmaker.data.repositories.AudioRepositoryImpl
import org.koin.dsl.module

import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.AudioRepository
import com.example.playlistmaker.domain.api.TracksRepository


val repositoryModule = module{

    single<TracksRepository>{
        TracksRepositoryImpl(get())
    }

    single<AppPrefsRepository>{
        AppPrefsRepositoryImpl(get(), get())
    }

    factory<AudioRepository>{
        AudioRepositoryImpl()
    }

}