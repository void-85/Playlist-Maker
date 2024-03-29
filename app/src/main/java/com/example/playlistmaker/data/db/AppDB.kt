package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        DBFavoriteTrackEntity::class,
        DBPlaylistEntity::class,
        DBTrackInPlaylist::class
    ]
)
abstract class AppDB : RoomDatabase() {

    abstract fun getDAO(): DBTrackDAO

}