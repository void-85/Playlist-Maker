package com.example.playlistmaker.domain.player



import com.example.playlistmaker.domain.api.repositories.AppPrefsRepository
import com.example.playlistmaker.domain.api.repositories.AudioRepository
import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.db.FavTracksPlaylistsRepository
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track


class MediaInteractorImpl(
    private val appPrefsRepositoryImpl: AppPrefsRepository,
    private val audioRepositoryImpl: AudioRepository,
    private val favTracksPlaylistsRepositoryImpl: FavTracksPlaylistsRepository
) : MediaInteractor {

    // APP PREFS REPOSITORY ---------------------------------------------------------------
    override fun getCurrentlyPlaying(): Track? {
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


    // FAV TRACKS & PLAYLISTS REPOSITORY --------------------------------------------------
    // ------ TRACKS ---------------------------------------------------
    override suspend fun insertTrack(track: Track) {
        favTracksPlaylistsRepositoryImpl.insertFavTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favTracksPlaylistsRepositoryImpl.deleteFavTrack(track)
    }

    override suspend fun isTrackFavorite(track: Track): Boolean {
        return favTracksPlaylistsRepositoryImpl.isTrackFavorite(track)
    }
    // ------ TRACKS ---------------------------------------------------


    // ------ PLAYLISTS ------------------------------------------------
    override suspend fun createPlaylist(playlist: Playlist) {
        favTracksPlaylistsRepositoryImpl.createPlaylist( playlist )
    }
    // ------ PLAYLISTS ------------------------------------------------
    // FAV TRACKS & PLAYLISTS REPOSITORY --------------------------------------------------
}