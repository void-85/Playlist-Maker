package com.example.playlistmaker.domain.entities

data class Playlist(

    val id: Long,

    val name: String,
    val description: String,

    val imageId: String,

    val tracks: List<Track>,
    val numberOfTracks: Int
)
