package com.example.playlistmaker.data.interLayerConverters

import com.example.playlistmaker.data.db.DBPlaylistEntity
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun Playlist.convert(): DBPlaylistEntity {
    return DBPlaylistEntity(

        id = this.id,

        name = this.name,
        description = this.description,

        imageId = this.imageId,

        tracksJson = Gson().toJson(this.tracks),
        numberOfTracks = this.numberOfTracks
    )
}

fun DBPlaylistEntity.convert(): Playlist {
    return Playlist(
        id = this.id,

        name = this.name,
        description = this.description,

        imageId = this.imageId,

        tracks = Gson().fromJson(
            this.tracksJson,
            object : TypeToken<List<Track>>() {}.type
        ),
        numberOfTracks = this.numberOfTracks
    )
}