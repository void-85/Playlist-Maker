package com.example.playlistmaker.domain.db


import com.example.playlistmaker.domain.entities.Track


interface FavTracksRepository {

    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun isTrackFavorite(trackId: Long): Boolean

}