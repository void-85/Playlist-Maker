package com.example.playlistmaker.domain.level_1_entities

interface AppPrefsRepository {

    fun isThemeDark() :Boolean
    fun setDarkTheme( darkThemeEnabled: Boolean): Unit

    fun getSearchHistory(): String
    fun setSearchHistory(text: String)

    fun isSearchHistoryEmpty(): Boolean
    fun setSearchHistoryEmpty( isEmpty :Boolean )

    fun getCurrentlyPlaying(): String
    fun setCurrentlyPlaying(text: String)

    fun getMediaPlayerLastPosition(): Long
    fun setMediaPlayerLastPosition(position: Long)

    fun isMediaPlayerToResumeOnCreate(): Boolean
    fun setMediaPlayerToResumeOnCreate(resume: Boolean)
}