package com.example.playlistmaker.domain.search


import com.example.playlistmaker.domain.api.repositories.AppPrefsRepository
import com.example.playlistmaker.domain.api.interactors.SearchInteractor
import com.example.playlistmaker.domain.api.repositories.TracksRepository
import com.example.playlistmaker.domain.db.FavoriteTracksAndPlaylistsRepository
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.utils.Option

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchInteractorImpl(
    private val appPrefsRepositoryImpl: AppPrefsRepository,
    private val tracksRepositoryImpl: TracksRepository,
    private val favoriteTracksAndPlaylistsRepositoryImpl: FavoriteTracksAndPlaylistsRepository
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
    override fun searchTracks(searchText: String): Flow<Pair<List<Track>?, String?>> {

        return tracksRepositoryImpl.searchTracks(searchText).map { result ->
            when (result) {
                is Option.Data -> {
                    Pair(result.data, null)
                }

                is Option.Error -> {
                    Pair(null, result.error)
                }
            }
        }
    }
    // TRACKS REPOSITORY ------------------------------------------------------------------


    // FAV TRACKS REPOSITORY --------------------------------------------------------------
    override suspend fun getAllFavoriteTracks(): Flow<Track>{
        return favoriteTracksAndPlaylistsRepositoryImpl.getAllFavTracks()
    }

    override suspend fun getAllFavoriteTracksIds(): Flow<Long> {
        return favoriteTracksAndPlaylistsRepositoryImpl.getAllFavTracksIds()
    }
    // FAV TRACKS REPOSITORY --------------------------------------------------------------
}