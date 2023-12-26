package com.example.playlistmaker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class DBPlaylistEntity(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,
    val description: String,

    @ColumnInfo(name = "image_id")
    val imageId: String,

    val tracksJson: String,
    val numberOfTracks: Int
)
