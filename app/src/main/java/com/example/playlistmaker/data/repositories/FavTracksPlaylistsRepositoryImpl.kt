package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.data.interLayerConverters.convert
import com.example.playlistmaker.domain.db.FavTracksPlaylistsRepository
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavTracksPlaylistsRepositoryImpl(
    private val appDB: AppDB
) : FavTracksPlaylistsRepository {

    // -- FAV TRACKS ----------------------------------------------------------
    override suspend fun insertFavTrack(track: Track) {
        appDB.getDAO().insertFavTrack(track.convert())
    }

    override suspend fun deleteFavTrack(track: Track) {
        appDB.getDAO().deleteFavTrack(track.convert().trackId)
    }

    override suspend fun isTrackFavorite(track: Track): Boolean {
        return appDB.getDAO().isTrackFavorite(track.convert().trackId) > 0
    }

    override suspend fun getAllFavTracks(): Flow<Track> = flow {
        appDB.getDAO().getAllFavTracks().forEach {
            emit(it.convert())
        }
    }

    override suspend fun getAllFavTracksIds(): Flow<Long> = flow {
        appDB.getDAO().getAllFavTracksIds().forEach {
            emit(it)
        }
    }
    // -- FAV TRACKS ----------------------------------------------------------


    // -- PLAYLISTS -----------------------------------------------------------
    override suspend fun createPlaylist(playlist: Playlist) {
        appDB.getDAO().createPlaylist( playlist.convert() )
    }

    override suspend fun getAllPlaylists(): Flow<Playlist> = flow {
        appDB.getDAO().getAllPlaylists().forEach {
            emit(it.convert())
        }
    }
    // -- PLAYLISTS -----------------------------------------------------------
}