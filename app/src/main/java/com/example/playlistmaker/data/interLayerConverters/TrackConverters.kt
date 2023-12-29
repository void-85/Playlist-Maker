package com.example.playlistmaker.data.interLayerConverters

import com.example.playlistmaker.data.db.DBTrackEntity
import com.example.playlistmaker.domain.entities.Track
import java.time.LocalDateTime
import java.time.ZoneOffset

/*fun Track.convert(): DBTrackEntity {
    return DBTrackEntity(

        this.trackId,

        this.trackName,
        this.artistName,
        this.trackTime,
        this.artworkUrl100,

        this.collectionName,
        this.releaseDate,
        this.primaryGenreName,
        this.country,

        this.previewUrl,

        LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    )
}

fun DBTrackEntity.convert(): Track{
    return Track(

        this.trackId,

        this.trackName,
        this.artistName,
        this.trackTime,
        this.artworkUrl100,

        this.collectionName,
        this.releaseDate,
        this.primaryGenreName,
        this.country,

        this.previewUrl,

        isFavorite = false
    )
}*/
