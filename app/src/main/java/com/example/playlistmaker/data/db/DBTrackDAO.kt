package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DBTrackDAO {

    // -- FAV TRACKS ----------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavTrack(track: DBFavoriteTrackEntity)

    @Query("DELETE FROM fav_tracks_table WHERE track_id = :trackId")
    suspend fun deleteFavTrack(trackId: Long)

    @Query("SELECT COUNT(*) FROM fav_tracks_table WHERE track_id = :trackId")
    suspend fun isTrackFavorite(trackId: Long): Int

    @Query("SELECT * FROM fav_tracks_table ORDER BY when_added DESC")
    suspend fun getAllFavTracks(): List<DBFavoriteTrackEntity>

    @Query("SELECT track_id FROM fav_tracks_table")
    suspend fun getAllFavTracksIds(): List<Long>
    // -- FAV TRACKS ----------------------------------------------------------


    // -- PLAYLISTS -----------------------------------------------------------

    @Insert
    suspend fun createPlaylist(playlist: DBPlaylistEntity)

    @Query("SELECT * FROM playlists_table ORDER BY name ASC")
    suspend fun getAllPlaylists(): List<DBPlaylistEntity>



    @Query("DELETE FROM tracks_in_playlists_table WHERE playlist_id = :playlistId")
    suspend fun deletePlaylistTracks(playlistId: Long)

    @Query("DELETE FROM playlists_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)



    @Query("SELECT * FROM playlists_table WHERE id = :playlistId")
    suspend fun getPlaylist(playlistId: Long): DBPlaylistEntity

    @Query("SELECT * FROM tracks_in_playlists_table WHERE playlist_id = :playlistId ORDER BY when_added_to_playlist DESC")
    suspend fun getPlaylistTracks(playlistId: Long): List<DBTrackInPlaylist>

    @Query("SELECT COUNT(*) FROM tracks_in_playlists_table WHERE playlist_id = :playlistId")
    suspend fun getTracksAmountInPlaylist(playlistId: Long): Int


    @Query(
        "UPDATE playlists_table SET " +
                "name = :name, " +
                "description = :desc, " +
                "image_id = :image " +
                "WHERE id = :playlistId"
    )
    suspend fun modifyPlaylistById(playlistId: Long, name: String, desc: String, image: String)



    @Query("SELECT COUNT(*) FROM tracks_in_playlists_table WHERE track_id = :trackId AND playlist_id = :playlistId")
    suspend fun checkIfTrackIsInPlaylist(trackId:Long, playlistId: Long):Int

    @Insert
    suspend fun addTrackToPlaylist(trackInPlaylist: DBTrackInPlaylist)

    @Query("DELETE FROM tracks_in_playlists_table WHERE track_id = :trackId AND playlist_id = :playlistId")
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    // -- PLAYLISTS -----------------------------------------------------------
}