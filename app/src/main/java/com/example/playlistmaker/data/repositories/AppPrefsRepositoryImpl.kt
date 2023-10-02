package com.example.playlistmaker.data.repositories


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.AppPrefsRepository

class AppPrefsRepositoryImpl : AppPrefsRepository {

    private lateinit var sharedPrefs: SharedPreferences
    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences(App.PLAYLIST_PREFERENCES, MODE_PRIVATE)
    }


    override fun isThemeDark(): Boolean {
        return sharedPrefs.getBoolean(App.CURRENT_THEME_KEY, false)
    }

    override fun setDarkTheme(darkThemeEnabled: Boolean) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putBoolean(App.CURRENT_THEME_KEY, darkThemeEnabled)
                .apply()
        }
    }


    override fun getSearchHistory(): String {
        return sharedPrefs.getString(App.SEARCH_HISTORY_KEY, "").orEmpty()
    }

    override fun setSearchHistory(text: String) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putString(App.SEARCH_HISTORY_KEY, text)
                .apply()
        }
    }


    override fun isSearchHistoryEmpty(): Boolean {
        return sharedPrefs.getBoolean(App.IS_SEARCH_HISTORY_EMPTY, true)
    }

    override fun setSearchHistoryEmpty(isEmpty: Boolean) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putBoolean(App.IS_SEARCH_HISTORY_EMPTY, isEmpty)
                .apply()
        }
    }


    override fun getCurrentlyPlaying(): String {
        return sharedPrefs.getString(App.CURRENTLY_PLAYING_KEY, "") ?: ""
    }

    override fun setCurrentlyPlaying(text: String) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putString(App.CURRENTLY_PLAYING_KEY, text)
                .apply()
        }
    }

    override fun getMediaPlayerLastPosition(): Long {
        return sharedPrefs.getLong(App.MEDIA_PLAYER_LAST_POSITION_LONG_KEY, 0L)
    }

    override fun setMediaPlayerLastPosition(position: Long) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putLong(App.MEDIA_PLAYER_LAST_POSITION_LONG_KEY, position)
                .apply()
        }
    }

    override fun isMediaPlayerToResumeOnCreate(): Boolean {
        return sharedPrefs.getBoolean(App.MEDIA_PLAYER_RESUME_PLAY_ON_CREATE, false)
    }

    override fun setMediaPlayerToResumeOnCreate(resume: Boolean) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putBoolean(App.MEDIA_PLAYER_RESUME_PLAY_ON_CREATE, resume)
                .apply()
        }
    }
}