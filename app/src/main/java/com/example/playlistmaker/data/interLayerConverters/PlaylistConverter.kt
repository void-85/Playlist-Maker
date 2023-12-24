package com.example.playlistmaker.data.interLayerConverters

import com.example.playlistmaker.data.db.DBPlaylistEntity
import com.example.playlistmaker.domain.entities.Playlist
import com.google.gson.Gson

fun Playlist.convert(): DBPlaylistEntity {
    return DBPlaylistEntity(

        id = this.id,

        name = this.name,
        description = this.description,

        imageId = this.imageId,

        tracksJson = Gson().toJson( this.tracks ),
        numberOfTracks = this.numberOfTracks
    )
}