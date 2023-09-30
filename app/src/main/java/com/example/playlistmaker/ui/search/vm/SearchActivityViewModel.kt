package com.example.playlistmaker.ui.search.vm



import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.api.IntentInteractor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.intentInteractor
import com.example.playlistmaker.searchInteractor



class SearchActivityViewModel(
    private val searchInteractor: SearchInteractor,
    private val intentInteractor: IntentInteractor
) : ViewModel() {

    private var screenUpdate = MutableLiveData<SearchActivityUpdate>(SearchActivityUpdate.Loading)

    init {

        val json = searchInteractor.getSearchHistory()
        if (json.isNotEmpty()) {

            val data = ArrayList<Track>()

            Gson().fromJson<ArrayList<Track>>(
                json,
                object : TypeToken<ArrayList<Track>>() {}.type
            ).forEach {
                data.add(it)
            }

            screenUpdate.postValue( SearchActivityUpdate.SearchHistoryData( data ))
        }else{
            screenUpdate.postValue( SearchActivityUpdate.SearchHistoryData( emptyList() ) )
        }
    }


    fun getState(): LiveData<SearchActivityUpdate> {
        return screenUpdate
    }

    fun clearSearchHistory(){
        searchInteractor.setSearchHistory("")
    }

    fun sendIntent(intent: Intent) {
        intentInteractor.sendIntent(intent)
    }

    fun searchTracks( searchText:String ){

        screenUpdate.postValue( SearchActivityUpdate.Loading )

        val data = ArrayList<Track>()

        searchInteractor.searchTracks(
            searchText,
            object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    foundTracks.forEach { data.add(it) }

                    screenUpdate.postValue( SearchActivityUpdate.SearchResult( data ) )
                }
            }
        )

    }

    fun saveSearchHistoryAndCurrentlyplaying(
        historyData: String,
        currentlyPlaying: String
    ) {
        searchInteractor.setSearchHistory(historyData)
        searchInteractor.setCurrentlyPlaying(currentlyPlaying)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchActivityViewModel(searchInteractor, intentInteractor)
                }
            }
    }
}


sealed class SearchActivityUpdate{

    object Loading: SearchActivityUpdate()

    data class SearchHistoryData(
        val tracks: List<Track>
    ) : SearchActivityUpdate()

    data class SearchResult(
        val tracks: List<Track>
    ) : SearchActivityUpdate()
}
