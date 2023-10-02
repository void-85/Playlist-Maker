package com.example.playlistmaker.data.repositories


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.entities.Track


class AppPrefsRepositoryImpl : AppPrefsRepository {

    companion object{
        const val PLAYLIST_PREFERENCES = "playlist_preferences"

        const val CURRENT_THEME_KEY = "current_theme_key"
        const val SEARCH_HISTORY_KEY = "search_history_key"
        const val IS_SEARCH_HISTORY_EMPTY = "is_search_history_empty"
        const val CURRENTLY_PLAYING_KEY = "currently_playing_key"

        const val MEDIA_PLAYER_LAST_POSITION_LONG_KEY = "media_player_last_position_long_key"
        const val MEDIA_PLAYER_RESUME_PLAY_ON_CREATE = "media_player_resume_play_on_create"
    }

    private lateinit var sharedPrefs: SharedPreferences
    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences(PLAYLIST_PREFERENCES, MODE_PRIVATE)
    }


    override fun isThemeDark(): Boolean {
        return sharedPrefs.getBoolean(CURRENT_THEME_KEY, false)
    }

    override fun setDarkTheme(darkThemeEnabled: Boolean) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putBoolean(CURRENT_THEME_KEY, darkThemeEnabled)
                .apply()
        }
    }


    override fun getSearchHistory(): List<Track> {

        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, "").orEmpty()
        return if (json.isNotEmpty()) {
            val data = ArrayList<Track>()

            Gson().fromJson<ArrayList<Track>>(
                json,
                object : TypeToken<ArrayList<Track>>() {}.type
            ).forEach { data.add(it) }

            data

        }else{
            emptyList()
        }
    }

    override fun setSearchHistory(tracks: List<Track>) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putString(SEARCH_HISTORY_KEY, Gson().toJson(tracks))
                .apply()
        }
    }


    /*override fun isSearchHistoryEmpty(): Boolean {
        return sharedPrefs.getBoolean(IS_SEARCH_HISTORY_EMPTY, true)
    }

    override fun setSearchHistoryEmpty(isEmpty: Boolean) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putBoolean(IS_SEARCH_HISTORY_EMPTY, isEmpty)
                .apply()
        }
    }*/


    override fun getCurrentlyPlaying(): Track? {

        val jsonTrack = sharedPrefs.getString(CURRENTLY_PLAYING_KEY, "") ?: ""
        if( jsonTrack.isNotEmpty() ){

            val data = Gson().fromJson<Track>(
                jsonTrack,
                object : TypeToken<Track>() {}.type
            )
            return data

        }else{
            return null
        }
    }

    override fun setCurrentlyPlaying(track: Track) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putString(CURRENTLY_PLAYING_KEY, Gson().toJson(track) )
                .apply()
        }
    }

    override fun getMediaPlayerLastPosition(): Long {
        return sharedPrefs.getLong(MEDIA_PLAYER_LAST_POSITION_LONG_KEY, 0L)
    }

    override fun setMediaPlayerLastPosition(position: Long) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putLong(MEDIA_PLAYER_LAST_POSITION_LONG_KEY, position)
                .apply()
        }
    }

    override fun isMediaPlayerToResumeOnCreate(): Boolean {
        return sharedPrefs.getBoolean(MEDIA_PLAYER_RESUME_PLAY_ON_CREATE, false)
    }

    override fun setMediaPlayerToResumeOnCreate(resume: Boolean) {
        synchronized(sharedPrefs) {
            sharedPrefs
                .edit()
                .putBoolean(MEDIA_PLAYER_RESUME_PLAY_ON_CREATE, resume)
                .apply()
        }
    }
}