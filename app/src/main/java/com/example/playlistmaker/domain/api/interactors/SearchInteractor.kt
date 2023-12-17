package com.example.playlistmaker.domain.api.interactors

import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    // APP PREFS REPOSITORY ---------------------------------------------------------------
    fun getSearchHistory(): List<Track>
    fun setSearchHistory(tracks: List<Track>)
    fun setCurrentlyPlaying(track: Track)
    // APP PREFS REPOSITORY ---------------------------------------------------------------


    // TRACKS REPOSITORY ------------------------------------------------------------------
    fun searchTracks(searchText: String): Flow<Pair<List<Track>?, String?>>
    // TRACKS REPOSITORY ------------------------------------------------------------------


    // FAV TRACKS REPOSITORY --------------------------------------------------------------

    // FAV TRACKS REPOSITORY --------------------------------------------------------------
}