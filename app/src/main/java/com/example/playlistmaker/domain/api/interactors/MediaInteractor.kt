package com.example.playlistmaker.domain.api.interactors

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow

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


    // FAV TRACKS & PLAYLISTS REPOSITORY --------------------------------------------------
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun isTrackFavorite(track: Track): Boolean

    suspend fun createPlaylist(playlist: Playlist)
    suspend fun getAllPlaylists(): Flow<Playlist>
    // FAV TRACKS & PLAYLISTS REPOSITORY --------------------------------------------------
}


