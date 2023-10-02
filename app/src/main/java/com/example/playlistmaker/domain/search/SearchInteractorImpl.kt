package com.example.playlistmaker.domain.search


import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.entities.Track


class SearchInteractorImpl(
    private val appPrefsRepositoryImpl: AppPrefsRepository,
    private val tracksRepositoryImpl: TracksRepository
) : SearchInteractor {

    // APP PREFS REPOSITORY ---------------------------------------------------------------
    override fun getSearchHistory(): List<Track> {
        return appPrefsRepositoryImpl.getSearchHistory()
    }

    override fun setSearchHistory(text: String) {
        appPrefsRepositoryImpl.setSearchHistory(text)
    }

    override fun setCurrentlyPlaying(text: String) {
        appPrefsRepositoryImpl.setCurrentlyPlaying(text)
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