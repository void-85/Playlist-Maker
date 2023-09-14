package com.example.playlistmaker.domain.level_1_entities

interface AppPrefsRepository {

    fun isThemeDark() :Boolean
    fun setDarkTheme( darkThemeEnabled: Boolean): Unit

}