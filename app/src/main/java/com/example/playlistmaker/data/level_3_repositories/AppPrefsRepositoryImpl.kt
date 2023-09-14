package com.example.playlistmaker.data.level_3_repositories


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.level_1_entities.AppPrefsRepository


class AppPrefsRepositoryImpl() :AppPrefsRepository {

    private lateinit var sharedPrefs :SharedPreferences
    fun init( context: Context ){
        sharedPrefs = context.getSharedPreferences( App.PLAYLIST_PREFERENCES, MODE_PRIVATE )
    }

    override fun isThemeDark(): Boolean {
        return sharedPrefs.getBoolean(App.CURRENT_THEME_KEY, false)
    }

    override fun setDarkTheme( darkThemeEnabled :Boolean ){
        synchronized( sharedPrefs ) {
            sharedPrefs
                .edit()
                .putBoolean(App.CURRENT_THEME_KEY, darkThemeEnabled)
                .apply()
        }
    }

}