package com.example.playlistmaker.domain.search


import com.example.playlistmaker.domain.api.repositories.AppPrefsRepository
import com.example.playlistmaker.domain.api.interactors.SearchInteractor
import com.example.playlistmaker.domain.api.repositories.TracksRepository
import com.example.playlistmaker.domain.entities.Track


class SearchInteractorImpl(
    private val appPrefsRepositoryImpl: AppPrefsRepository,
    private val tracksRepositoryImpl: TracksRepository
) : SearchInteractor {

    // APP PREFS REPOSITORY ---------------------------------------------------------------
    override fun getSearchHistory(): List<Track> {
        return appPrefsRepositoryImpl.getSearchHistory()
    }

    override fun setSearchHistory(tracks: List<Track>) {
        appPrefsRepositoryImpl.setSearchHistory(tracks)
    }

    override fun setCurrentlyPlaying(track: Track) {
        appPrefsRepositoryImpl.setCurrentlyPlaying(track)
    }
    // APP PREFS REPOSITORY ---------------------------------------------------------------


    // TRACKS REPOSITORY ------------------------------------------------------------------
    override fun searchTracks(
        searchText: String,
        consumer: SearchInteractor.TracksConsumer
    ) {
        val t = Thread {
            consumer.consume(
                tracksRepositoryImpl.searchTracks(searchText)
            )
        }
        t.start()
        t.join()
    }
    // TRACKS REPOSITORY ------------------------------------------------------------------
}