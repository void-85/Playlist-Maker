package com.example.playlistmaker.domain.api
import com.example.playlistmaker.domain.entities.Track

interface SearchInteractor {

    // APP PREFS REPOSITORY ---------------------------------------------------------------
    fun getSearchHistory(): List<Track>
    fun setSearchHistory(tracks: List<Track>)

    fun setCurrentlyPlaying(track: Track)
    // APP PREFS REPOSITORY ---------------------------------------------------------------


    // TRACKS REPOSITORY ------------------------------------------------------------------
    fun searchTracks(searchText: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
    // TRACKS REPOSITORY ------------------------------------------------------------------
}