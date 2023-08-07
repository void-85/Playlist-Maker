package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate



class App :Application() {


    companion object{
        const val PLAYLIST_PREFERENCES = "playlist_preferences"

        const val CURRENT_THEME_KEY       = "current_theme_key"
        const val SEARCH_HISTORY_KEY      = "search_history_key"
        const val IS_SEARCH_HISTORY_EMPTY = "is_search_history_empty"
        const val CURRENTLY_PLAYING_KEY   = "currently_playing_key"

        const val SEARCH_HISTORY_MAX_LENGTH = 10
    }

    private var darkTheme = false



    override fun onCreate(){

        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_PREFERENCES, MODE_PRIVATE)
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