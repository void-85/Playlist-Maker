package com.example.playlistmaker.data.interLayerConverters

import com.example.playlistmaker.data.db.DBTrackEntity
import com.example.playlistmaker.domain.entities.Track


/*fun TrackDTO.convert(): DBTrackEntity {
    return DBTrackEntity(

        this.trackId,

        this.trackName ?: "",
        this.artistName ?: "",
        this.trackTimeMillis?.millisToMinSec() ?: "0:00",
        this.artworkUrl100 ?: "",

        this.collectionName ?: "",
        this.releaseDate ?: "",
        this.primaryGenreName ?: "",
        this.country ?: "",

        this.previewUrl ?: ""
    )
}*/

/*fun DBTrackEntity.convert(): Track {
    return Track(

        this.trackId,

        this.isFavorite,
    
        this.trackName,
        this.artistName,
        this.trackTime,
        this.artworkUrl100,
    
        this.collectionName,
        this.releaseDate,
        this.primaryGenreName,
        this.country,
    
        this.previewUrl
    )
}*/

fun Track.convert(): DBTrackEntity{
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
    
        this.previewUrl
    )
}
