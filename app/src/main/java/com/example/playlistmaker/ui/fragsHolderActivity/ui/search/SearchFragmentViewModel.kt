package com.example.playlistmaker.ui.fragsHolderActivity.ui.search


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.playlistmaker.domain.api.interactors.SearchInteractor
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragmentViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var screenUpdateLiveData =
        MutableLiveData<SearchFragmentUpdate>(SearchFragmentUpdate.DoNothing)
    private var searchJob: Job? = null

    init {
        requestSearchHistory()
    }


    fun getState(): LiveData<SearchFragmentUpdate> {
        return screenUpdateLiveData
    }

    fun clearSearchHistory() {
        searchInteractor.setSearchHistory(emptyList())
        screenUpdateLiveData.postValue(SearchFragmentUpdate.DoNothing)
    }

    fun cancelSearch() {
        searchJob?.cancel()
    }

    fun requestSearchHistory() {
        screenUpdateLiveData.postValue(
            SearchFragmentUpdate.SearchHistoryData(
                searchInteractor.getSearchHistory()
            )
        )
    }

    fun updateRecieved() {
        screenUpdateLiveData.postValue(SearchFragmentUpdate.DoNothing)
    }

    fun searchTracksDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            searchTracks(searchText)
        }
    }

    private fun searchTracks(searchText: String) {

        if (searchText.length >= SEARCH_DEBOUNCE_REQ_MIN_LEN) {

            viewModelScope.launch {
                searchInteractor.searchTracks(searchText).collect { pair ->
                    run {

                        if (pair.first == null) {

                            screenUpdateLiveData.postValue(SearchFragmentUpdate.NoNetwork)

                        } else {

                            /*                            val favIds = ArrayList<Long>()
                                                        searchInteractor.getAllFavoriteTracksIds().collect{
                                                            favIds.add(it)
                                                        }*/

                            val tracks = ArrayList<Track>()
                            pair.first?.forEach {

                                tracks.add(it) //it.apply { isFavorite = favIds.contains(it.trackId) } )
                            }

                            screenUpdateLiveData.postValue(SearchFragmentUpdate.SearchResult(tracks))

                        }
                    }
                }
            }
        }
    }


    fun saveSearchHistoryAndCurrentlyPlaying(
        historyData: List<Track>,
        currentlyPlaying: Track
    ) {
        searchInteractor.setSearchHistory(historyData)
        searchInteractor.setCurrentlyPlaying(currentlyPlaying)
    }


    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2_000L
        const val SEARCH_DEBOUNCE_REQ_MIN_LEN = 3
    }
}


sealed class SearchFragmentUpdate {

    object DoNothing : SearchFragmentUpdate()

    object NoNetwork : SearchFragmentUpdate()

    object Loading : SearchFragmentUpdate()

    data class SearchHistoryData(
        val tracks: List<Track>
    ) : SearchFragmentUpdate()

    data class SearchResult(
        val tracks: List<Track>
    ) : SearchFragmentUpdate()
}


