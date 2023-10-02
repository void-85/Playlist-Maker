package com.example.playlistmaker.ui.search.vm


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.entities.Track


class SearchActivityViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var screenUpdate = MutableLiveData<SearchActivityUpdate>(SearchActivityUpdate.Loading)

    init {
        screenUpdate.postValue(
            SearchActivityUpdate.SearchHistoryData(
                searchInteractor.getSearchHistory()
            )
        )
    }

    private val handler = Handler(Looper.getMainLooper())
    fun searchDebounce( runnable: Runnable) {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, TracksRepositoryImpl.SEARCH_DEBOUNCE_DELAY)
    }




    fun getState(): LiveData<SearchActivityUpdate> {
        return screenUpdate
    }

    fun clearSearchHistory() {
        searchInteractor.setSearchHistory(emptyList())
    }

    fun searchTracks(searchText: String) {

        screenUpdate.postValue(SearchActivityUpdate.Loading)

        val data = ArrayList<Track>()

        searchInteractor.searchTracks(
            searchText,
            object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    foundTracks.forEach { data.add(it) }

                    screenUpdate.postValue(SearchActivityUpdate.SearchResult(data))
                }
            }
        )

    }

    fun saveSearchHistoryAndCurrentlyPlaying(
        historyData: List<Track>,
        currentlyPlaying: Track
    ) {
        searchInteractor.setSearchHistory(historyData)
        searchInteractor.setCurrentlyPlaying(currentlyPlaying)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchActivityViewModel(Creator.provideSearchInteractor())
                }
            }
    }
}


sealed class SearchActivityUpdate {

    object Loading : SearchActivityUpdate()

    data class SearchHistoryData(
        val tracks: List<Track>
    ) : SearchActivityUpdate()

    data class SearchResult(
        val tracks: List<Track>
    ) : SearchActivityUpdate()
}
