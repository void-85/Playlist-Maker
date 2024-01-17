package com.example.playlistmaker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists_table")
data class DBTrackInPlaylist(

    @PrimaryKey(autoGenerate = true)
    val recordId: Long,

    @ColumnInfo(name = "track_id")
    val trackId: Long,

    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,

    @ColumnInfo(name = "json_data")
    val jsonData: String,

    @ColumnInfo(name = "when_added_to_playlist")
    val whenAddedToPlaylist: Long
)
