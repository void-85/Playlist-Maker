package com.example.playlistmaker.domain.player



import com.example.playlistmaker.domain.api.repositories.AppPrefsRepository
import com.example.playlistmaker.domain.api.repositories.AudioRepository
import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksAndPlaylistsRepository
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow


class MediaInteractorImpl(
    private val appPrefsRepositoryImpl: AppPrefsRepository,
    private val audioRepositoryImpl: AudioRepository,
    private val favoriteTracksAndPlaylistsRepositoryImpl: FavoriteTracksAndPlaylistsRepository
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
        favoriteTracksAndPlaylistsRepositoryImpl.insertFavTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteTracksAndPlaylistsRepositoryImpl.deleteFavTrack(track)
    }

    override suspend fun isTrackFavorite(track: Track): Boolean {
        return favoriteTracksAndPlaylistsRepositoryImpl.isTrackFavorite(track)
    }
    // ------ TRACKS ---------------------------------------------------


    // ------ PLAYLISTS ------------------------------------------------
    override suspend fun createPlaylist(playlist: Playlist) {
        favoriteTracksAndPlaylistsRepositoryImpl.createPlaylist(playlist)
    }

    override suspend fun getAllPlaylistsWithoutTracksData(): Flow<Playlist> {
        return favoriteTracksAndPlaylistsRepositoryImpl.getAllPlaylistsWithoutTracksData()
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        favoriteTracksAndPlaylistsRepositoryImpl.deletePlaylist(playlistId)
    }

    override suspend fun getPlaylist(playlistId: Long): Playlist {
        return favoriteTracksAndPlaylistsRepositoryImpl.getPlaylist(playlistId)
    }

    override suspend fun updatePlaylistInfo(playlist: Playlist) {
        return favoriteTracksAndPlaylistsRepositoryImpl.updatePlaylistInfo(playlist)
    }

    override suspend fun checkIfTrackIsInPlaylist(trackId: Long, playlistId: Long): Boolean {
        return favoriteTracksAndPlaylistsRepositoryImpl.checkIfTrackIsInPlaylist(trackId, playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        favoriteTracksAndPlaylistsRepositoryImpl.addTrackToPlaylist(track, playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        favoriteTracksAndPlaylistsRepositoryImpl.deleteTrackFromPlaylist(trackId, playlistId)
    }
    // ------ PLAYLISTS ------------------------------------------------
    // FAV TRACKS & PLAYLISTS REPOSITORY --------------------------------------------------
}