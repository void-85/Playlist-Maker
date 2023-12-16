package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(version = 1, entities = [DBTrackEntity::class])
abstract class AppDB :RoomDatabase() {

    abstract fun getDAO(): DBTrackDAO

}