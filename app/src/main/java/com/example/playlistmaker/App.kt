package com.example.playlistmaker


import android.app.Application
import com.example.playlistmaker.creator.Creator


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Creator.init(this) // (App context) -> App ==> Creator ==> AppPrefsRepository ==> SharedPrefs
    }
}