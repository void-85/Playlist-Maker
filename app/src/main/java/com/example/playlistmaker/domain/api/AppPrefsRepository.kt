package com.example.playlistmaker.domain.api

interface AppPrefsRepository {

    fun isThemeDark(): Boolean
    fun setDarkTheme(darkThemeEnabled: Boolean)

    fun getSearchHistory(): String
    fun setSearchHistory(text: String)

    fun isSearchHistoryEmpty(): Boolean
    fun setSearchHistoryEmpty(isEmpty: Boolean)

    fun getCurrentlyPlaying(): String
    fun setCurrentlyPlaying(text: String)

    fun getMediaPlayerLastPosition(): Long
    fun setMediaPlayerLastPosition(position: Long)

    fun isMediaPlayerToResumeOnCreate(): Boolean
    fun setMediaPlayerToResumeOnCreate(resume: Boolean)
}