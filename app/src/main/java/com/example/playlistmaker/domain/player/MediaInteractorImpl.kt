package com.example.playlistmaker.domain.player


import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.AudioRepository
import com.example.playlistmaker.domain.api.MediaInteractor


class MediaInteractorImpl(
    private val appPrefsRepositoryImpl: AppPrefsRepository,
    private val audioRepositoryImpl: AudioRepository
) : MediaInteractor {

    // APP PREFS REPOSITORY ---------------------------------------------------------------
    override fun getCurrentlyPlaying(): String {
        return appPrefsRepositoryImpl.getCurrentlyPlaying()
    }


    override fun getMediaPlayerLastPosition(): Long {
        return appPrefsRepositoryImpl.getMediaPlayerLastPosition()
    }

    override fun setMediaPlayerLastPosition(position: Long) {
        appPrefsRepositoryImpl.setMediaPlayerLastPosition(position)
    }


    override fun isMediaPlayerToResumeOnCreate(): Boolean {
        return appPrefsRepositoryImpl.isMediaPlayerToResumeOnCreate()
    }

    override fun setMediaPlayerToResumeOnCreate(resume: Boolean) {
        appPrefsRepositoryImpl.setMediaPlayerToResumeOnCreate(resume)
    }
    // APP PREFS REPOSITORY ---------------------------------------------------------------


    // AUDIO REPOSITORY -------------------------------------------------------------------
    override fun getCurrentPosition(): Long {
        return audioRepositoryImpl.getCurrentPosition()
    }

    override fun isPlaying(): Boolean {
        return audioRepositoryImpl.isPlaying()
    }

    override fun prepare(
        url: String,
        seekToWhenPrepared: Int,
        autoPlay: Boolean,
        updateFun: () -> Unit,
        onCompletionFun: () -> Unit,
        onPlayFun: () -> Unit,
        onPauseFun: () -> Unit
    ) {
        audioRepositoryImpl.prepare(
            url,
            seekToWhenPrepared,
            autoPlay,
            updateFun,
            onCompletionFun,
            onPlayFun,
            onPauseFun
        )
    }

    override fun start() {
        audioRepositoryImpl.start()
    }

    override fun pause() {
        audioRepositoryImpl.pause()
    }

    override fun release() {
        audioRepositoryImpl.release()
    }
    // AUDIO REPOSITORY -------------------------------------------------------------------
}