package com.example.playlistmaker.di


import android.content.Context
import org.koin.dsl.module

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

import com.google.gson.Gson

import com.example.playlistmaker.data.api.SearchAPIService
import com.example.playlistmaker.data.web.RetrofitNetworkClient
import com.example.playlistmaker.data.api.NetworkClient
import org.koin.android.ext.koin.androidContext


val dataModule = module{

    single<SearchAPIService>{
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(SearchAPIService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    factory { Gson() }

    single {
        androidContext().getSharedPreferences(
            "playlist_preferences",
            Context.MODE_PRIVATE
        )
    }
}