package com.example.playlistmaker.domain.db


import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow


interface FavTracksRepository {

    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun isTrackFavorite(track: Track): Boolean
    suspend fun getAllTracks(): Flow<Track>

}