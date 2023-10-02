package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entities.Track

interface AppPrefsRepository {

    fun isThemeDark(): Boolean
    fun setDarkTheme(darkThemeEnabled: Boolean)

    fun getSearchHistory(): String
    fun setSearchHistory(text: String)

    fun isSearchHistoryEmpty(): Boolean
    fun setSearchHistoryEmpty(isEmpty: Boolean)

    fun getCurrentlyPlaying(): Track?
    fun setCurrentlyPlaying(text: String)

    fun getMediaPlayerLastPosition(): Long
    fun setMediaPlayerLastPosition(position: Long)

    fun isMediaPlayerToResumeOnCreate(): Boolean
    fun setMediaPlayerToResumeOnCreate(resume: Boolean)
}