package com.example.playlistmaker.domain.useCases


import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.AudioRepository
import com.example.playlistmaker.domain.api.Interactor
import com.example.playlistmaker.domain.api.TracksRepository


class InteractorImpl (
    private val appPrefsRepositoryImpl : AppPrefsRepository,
    private val tracksRepositoryImpl   : TracksRepository,
    private val audioRepositoryImpl    : AudioRepository
) : Interactor
{

    // APP PREFS REPOSITORY ---------------------------------------------------------------
    override fun getSearchHistory(): String {
        return appPrefsRepositoryImpl.getSearchHistory()
    }
    override fun setSearchHistory(text: String) {
        appPrefsRepositoryImpl.setSearchHistory(text)
    }


    override fun isSearchHistoryEmpty(): Boolean {
        return appPrefsRepositoryImpl.isSearchHistoryEmpty()
    }
    override fun setSearchHistoryEmpty(isEmpty: Boolean) {
        appPrefsRepositoryImpl.setSearchHistoryEmpty(isEmpty)
    }


    override fun getCurrentlyPlaying(): String {
        return appPrefsRepositoryImpl.getCurrentlyPlaying()
    }
    override fun setCurrentlyPlaying(text: String) {
        appPrefsRepositoryImpl.setCurrentlyPlaying(text)
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



    // TRACKS REPOSITORY ------------------------------------------------------------------
    override fun searchTracks(searchText: String, consumer: Interactor.TracksConsumer) {

        val t = Thread{ consumer.consume( tracksRepositoryImpl.searchTracks( searchText ) ) }
        t.start()
        t.join()
    }
    // TRACKS REPOSITORY ------------------------------------------------------------------



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