package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DBTrackDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: DBTrackEntity)

    //@Delete
    @Query("DELETE FROM fav_tracks_table WHERE track_id = :trackId")
    suspend fun deleteTrack(trackId: Long)

    @Query("SELECT COUNT(*) FROM fav_tracks_table WHERE track_id = :trackId")
    suspend fun isTrackFavorite(trackId: Long): Int

}