package com.example.playlistmaker.di


import android.content.Context
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

import com.google.gson.Gson

import com.example.playlistmaker.data.api.SearchAPIService
import com.example.playlistmaker.data.web.RetrofitNetworkClient
import com.example.playlistmaker.data.api.NetworkClient


class Constants {
    companion object {
        const val SERVICE_URL = "https://itunes.apple.com"
        const val SHARED_PREFS = "playlist_preferences"
    }
}


val dataModule = module {

    single<SearchAPIService> {
        Retrofit.Builder()
            .baseUrl(Constants.SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(SearchAPIService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    factory { Gson() }

    single {
        androidContext().getSharedPreferences(
            Constants.SHARED_PREFS,
            Context.MODE_PRIVATE
        )
    }
}