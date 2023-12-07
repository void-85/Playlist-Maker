package com.example.playlistmaker.domain.api.interactors

import com.example.playlistmaker.domain.entities.Track

interface MediaInteractor {

    // AUDIO REPOSITORY -------------------------------------------------------------------
    fun getCurrentPosition(): Long
    fun isPlaying(): Boolean

    fun prepare(
        url: String,
        seekToWhenPrepared: Int,
        autoPlay: Boolean,
        updateFun: () -> (Unit),
        onCompletionFun: () -> (Unit),
        onPlayFun: () -> (Unit),
        onPauseFun: () -> (Unit)
    )

    fun start()
    fun pause()
    fun release()
    // AUDIO REPOSITORY -------------------------------------------------------------------


    // APP PREFS REPOSITORY ---------------------------------------------------------------
    fun isMediaPlayerToResumeOnCreate(): Boolean
    fun setMediaPlayerToResumeOnCreate(resume: Boolean)

    fun getMediaPlayerLastPosition(): Long
    fun setMediaPlayerLastPosition(position: Long)

    fun getCurrentlyPlaying(): Track?
    // APP PREFS REPOSITORY ---------------------------------------------------------------
}


