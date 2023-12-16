package com.example.playlistmaker.data.db

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "fav_tracks_table")
data class DBTrackEntity(

    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId: Long,

    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,

    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,

    val previewUrl: String

)