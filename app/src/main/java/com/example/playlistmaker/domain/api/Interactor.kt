package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entities.Track

interface Interactor {

    // APP PREFS REPOSITORY ---------------------------------------------------------------
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
    // APP PREFS REPOSITORY ---------------------------------------------------------------



    // TRACKS REPOSITORY ------------------------------------------------------------------
    fun searchTracks( searchText :String, consumer : TracksConsumer)

    interface TracksConsumer{ fun consume( foundTracks :List<Track> ) }
    // TRACKS REPOSITORY ------------------------------------------------------------------



    // AUDIO REPOSITORY -------------------------------------------------------------------
    fun getCurrentPosition() :Long
    fun isPlaying() :Boolean

    fun prepare (
        url                :String       ,
        seekToWhenPrepared :Int          ,
        autoPlay           :Boolean      ,
        updateFun          :() -> (Unit) ,
        onCompletionFun    :() -> (Unit) ,
        onPlayFun          :() -> (Unit) ,
        onPauseFun         :() -> (Unit) )

    fun start()
    fun pause()
    fun release()
    // AUDIO REPOSITORY -------------------------------------------------------------------
}