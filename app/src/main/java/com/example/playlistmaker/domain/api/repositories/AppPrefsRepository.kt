package com.example.playlistmaker.domain.api.repositories

import com.example.playlistmaker.domain.entities.Track

interface AppPrefsRepository {

    fun isThemeDark(): Boolean
    fun setDarkTheme(darkThemeEnabled: Boolean)

    fun getSearchHistory(): List<Track>
    fun setSearchHistory(tracks: List<Track>)

    //fun isSearchHistoryEmpty(): Boolean
    //fun setSearchHistoryEmpty(isEmpty: Boolean)

    fun getCurrentlyPlaying(): Track?
    fun setCurrentlyPlaying(track: Track)

    fun getMediaPlayerLastPosition(): Long
    fun setMediaPlayerLastPosition(position: Long)

    fun isMediaPlayerToResumeOnCreate(): Boolean
    fun setMediaPlayerToResumeOnCreate(resume: Boolean)
}