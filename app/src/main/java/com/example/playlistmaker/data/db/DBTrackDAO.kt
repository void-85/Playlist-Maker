package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DBTrackDAO {

    // -- FAV TRACKS ----------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavTrack(track: DBTrackEntity)

    @Query("DELETE FROM fav_tracks_table WHERE track_id = :trackId")
    suspend fun deleteFavTrack(trackId: Long)

    @Query("SELECT COUNT(*) FROM fav_tracks_table WHERE track_id = :trackId")
    suspend fun isTrackFavorite(trackId: Long): Int

    @Query("SELECT * FROM fav_tracks_table ORDER BY when_added DESC")
    suspend fun getAllFavTracks(): List<DBTrackEntity>

    @Query("SELECT track_id FROM fav_tracks_table")
    suspend fun getAllFavTracksIds(): List<Long>
    // -- FAV TRACKS ----------------------------------------------------------



    // -- PLAYLISTS -----------------------------------------------------------
    @Insert
    suspend fun createPlaylist(playlist: DBPlaylistEntity)

    @Query("SELECT * FROM playlists_table ORDER BY image_id DESC")
    suspend fun getAllPlaylists(): List<DBPlaylistEntity>
    // -- PLAYLISTS -----------------------------------------------------------
}