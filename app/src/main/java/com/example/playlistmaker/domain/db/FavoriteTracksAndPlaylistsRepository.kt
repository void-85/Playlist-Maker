package com.example.playlistmaker.domain.db


import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow


interface FavoriteTracksAndPlaylistsRepository {

    // -- FAV TRACKS ----------------------------------------------------------
    suspend fun insertFavTrack(track: Track)
    suspend fun deleteFavTrack(track: Track)
    suspend fun isTrackFavorite(track: Track): Boolean
    suspend fun getAllFavTracks(): Flow<Track>
    suspend fun getAllFavTracksIds(): Flow<Long>
    // -- FAV TRACKS ----------------------------------------------------------

    // -- PLAYLISTS -----------------------------------------------------------
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun getAllPlaylistsWithoutTracksData(): Flow<Playlist>
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun getPlaylist(playlistId: Long): Playlist
    suspend fun updatePlaylistInfo(playlist: Playlist)
    suspend fun checkIfTrackIsInPlaylist(trackId: Long, playlistId: Long): Boolean
    suspend fun addTrackToPlaylist(track: Track, playlistId: Long)
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)
    // -- PLAYLISTS -----------------------------------------------------------
}