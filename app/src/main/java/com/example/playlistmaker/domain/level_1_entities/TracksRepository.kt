package com.example.playlistmaker.domain.level_1_entities

interface TracksRepository {
    fun searchTracks( searchText :String ) :List<Track>
}