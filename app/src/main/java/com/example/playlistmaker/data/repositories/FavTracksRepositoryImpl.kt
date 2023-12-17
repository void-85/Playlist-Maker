package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.data.interLayerConverters.convert
import com.example.playlistmaker.domain.db.FavTracksRepository
import com.example.playlistmaker.domain.entities.Track

class FavTracksRepositoryImpl(
    private val appDB: AppDB
) : FavTracksRepository {

    override suspend fun insertTrack(track: Track) {
        appDB.getDAO().insertTrack(track.convert())
    }

    override suspend fun deleteTrack(track: Track) {
        appDB.getDAO().deleteTrack(track.convert().trackId)
    }

    override suspend fun isTrackFavorite(track: Track): Boolean {
        return appDB.getDAO().isTrackFavorite(track.convert().trackId) > 0
    }
}