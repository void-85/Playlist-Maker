package com.example.playlistmaker.domain.api.repositories

import com.example.playlistmaker.domain.entities.Track

interface TracksRepository {
    fun searchTracks(searchText: String): List<Track>
}