package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate



class App :Application() {


    companion object{
        const val PLAYLIST_PREFERNCES = "playlist_preferences"
        const val CURRENT_THEME_KEY = "current_theme_key"
    }


    private var darkTheme = false



    override fun onCreate(){

        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_PREFERNCES, MODE_PRIVATE)
        switchTheme( sharedPrefs.getBoolean(CURRENT_THEME_KEY, false) )

    }



    fun switchTheme( darkThemeEnabled :Boolean ){

        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) { AppCompatDelegate.MODE_NIGHT_YES }
            else                  { AppCompatDelegate.MODE_NIGHT_NO  }
        )

    }
}