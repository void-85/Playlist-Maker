package com.example.playlistmaker.di


import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.player.MediaInteractorImpl
import com.example.playlistmaker.domain.search.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeInteractorImpl
import org.koin.dsl.module




val interactorModule = module{

    single<SearchInteractor>{
        SearchInteractorImpl(get(), get())
    }

    single<ThemeInteractor>{
        ThemeInteractorImpl(get())
    }

    single<MediaInteractor>{
        MediaInteractorImpl(get(), get())
    }

}