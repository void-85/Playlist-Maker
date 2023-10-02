package com.example.playlistmaker


import android.app.Application
import com.example.playlistmaker.creator.Creator


class App : Application() {

    companion object {
        const val PLAYLIST_PREFERENCES = "playlist_preferences"

        const val CURRENT_THEME_KEY = "current_theme_key"
        const val SEARCH_HISTORY_KEY = "search_history_key"
        const val IS_SEARCH_HISTORY_EMPTY = "is_search_history_empty"
        const val CURRENTLY_PLAYING_KEY = "currently_playing_key"

        const val MEDIA_PLAYER_LAST_POSITION_LONG_KEY = "media_player_last_position_long_key"
        const val MEDIA_PLAYER_RESUME_PLAY_ON_CREATE = "media_player_resume_play_on_create"

        const val SEARCH_HISTORY_MAX_LENGTH = 10
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
        const val SEARCH_DEBOUNCE_REQ_MIN_LEN = 3

        const val MEDIA_PLAYER_UPDATE_POS_PERIOD = 500L
    }

    override fun onCreate() {
        super.onCreate()

        Creator.init(this) // (App context) -> App ==> Creator ==> AppPrefsRepository ==> SharedPrefs
    }
}