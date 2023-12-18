package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DBTrackDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: DBTrackEntity)

    @Query("DELETE FROM fav_tracks_table WHERE track_id = :trackId")
    suspend fun deleteTrack(trackId: Long)

    @Query("SELECT COUNT(*) FROM fav_tracks_table WHERE track_id = :trackId")
    suspend fun isTrackFavorite(trackId: Long): Int

    @Query("SELECT * FROM fav_tracks_table ORDER BY when_added DESC")
    suspend fun getAllTracks(): List<DBTrackEntity>

    @Query("SELECT track_id FROM fav_tracks_table")
    suspend fun getAllTracksIds(): List<Long>
}