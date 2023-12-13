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

    private var screenUpdate = MutableLiveData<SearchActivityUpdate>(SearchActivityUpdate.DoNothing)
    private var searchJob: Job? = null

    init {
        requestSearchHistory()
    }

    fun getState(): LiveData<SearchActivityUpdate> {
        return screenUpdate
    }

    fun clearSearchHistory() {
        searchInteractor.setSearchHistory(emptyList())
        screenUpdate.postValue(SearchActivityUpdate.DoNothing)
    }

    fun cancelSearch() {
        searchJob?.cancel()
    }

    fun requestSearchHistory() {
        screenUpdate.postValue(
            SearchActivityUpdate.SearchHistoryData(
                searchInteractor.getSearchHistory()
            )
        )
    }

    fun searchTracksDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTracks(searchText)
        }
    }

    private fun     searchTracks(searchText: String) {

        if (searchText.length >= SEARCH_DEBOUNCE_REQ_MIN_LEN) {

/*            //screenUpdate.postValue(SearchActivityUpdate.Loading)
            val data = ArrayList<Track>()
            searchInteractor.searchTracks(
                searchText,
                object : SearchInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        foundTracks.forEach { data.add(it) }

                        screenUpdate.postValue(SearchActivityUpdate.SearchResult(data))
                    }
                }
            )*/

            val data = ArrayList<Track>()
            viewModelScope.launch { searchInteractor.searchTracks(searchText).collect{
                pair -> run {

                    if (pair.first == null) {

                        screenUpdate.postValue(SearchActivityUpdate.NoNetwork)

                    } else {

                        pair.first?.forEach { data.add(it) }
                        screenUpdate.postValue(SearchActivityUpdate.SearchResult(data))

                    }
                }
            }}


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
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
        const val SEARCH_DEBOUNCE_REQ_MIN_LEN = 3
    }
}


sealed class SearchActivityUpdate {

    object DoNothing : SearchActivityUpdate()

    object NoNetwork : SearchActivityUpdate()

    object Loading : SearchActivityUpdate()

    data class SearchHistoryData(
        val tracks: List<Track>
    ) : SearchActivityUpdate()

    data class SearchResult(
        val tracks: List<Track>
    ) : SearchActivityUpdate()
}


