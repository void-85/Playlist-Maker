package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate



class App :Application() {


    companion object {
        const val PLAYLIST_PREFERENCES    = "playlist_preferences"

        const val CURRENT_THEME_KEY       = "current_theme_key"
        const val SEARCH_HISTORY_KEY      = "search_history_key"
        const val IS_SEARCH_HISTORY_EMPTY = "is_search_history_empty"
        const val CURRENTLY_PLAYING_KEY   = "currently_playing_key"

        const val SEARCH_HISTORY_MAX_LENGTH   =    10
        const val SEARCH_DEBOUNCE_DELAY       = 2_000L
        const val SEARCH_DEBOUNCE_REQ_MIN_LEN =     3

        const val MEDIA_PLAYER_RESUME_PLAY_ON_CREATE_KEY = "media_player_resume_play_on_create_key"
        const val MEDIA_PLAYER_LAST_POSITION_LONG_KEY    = "media_player_last_position_long_key"

        const val MEDIA_PLAYER_UPDATE_POS_PERIOD = 300L
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