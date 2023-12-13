package com.example.playlistmaker.domain.api.repositories

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.utils.Option
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(searchText: String): Flow<Option<List<Track>>>
}