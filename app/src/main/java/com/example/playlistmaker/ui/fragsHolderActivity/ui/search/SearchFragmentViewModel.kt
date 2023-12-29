package com.example.playlistmaker.ui.fragsHolderActivity.ui.search


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
        MutableLiveData<SearchFragmentUpdate>(
            SearchFragmentUpdate.SearchHistoryData(searchInteractor.getSearchHistory())
        )
    private var searchJob: Job? = null

    fun getState(): LiveData<SearchFragmentUpdate> {
        return screenUpdateLiveData
    }

    fun clearSearchHistory() {
        searchInteractor.setSearchHistory(emptyList())
        screenUpdateLiveData.postValue(
            SearchFragmentUpdate.SearchHistoryData(searchInteractor.getSearchHistory())
        )
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

                            val tracks = ArrayList<Track>()
                            pair.first?.forEach {
                                tracks.add(it)
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

    object NoNetwork : SearchFragmentUpdate()

    data class SearchHistoryData(
        val tracks: List<Track>
    ) : SearchFragmentUpdate()

    data class SearchResult(
        val tracks: List<Track>
    ) : SearchFragmentUpdate()
}


