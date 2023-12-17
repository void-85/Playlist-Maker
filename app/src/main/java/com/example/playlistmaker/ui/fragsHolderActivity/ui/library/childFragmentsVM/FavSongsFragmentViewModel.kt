package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.interactors.SearchInteractor
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.launch


class FavSongsFragmentViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var state =
        MutableLiveData<FavSongsFragmentScreenUpdate>(
            FavSongsFragmentScreenUpdate.ShowNoData
        )

    init {
        viewModelScope.launch {
            val favTracks = ArrayList<Track>()

            searchInteractor.getAllFavoriteTracks().collect{
                favTracks.add( it )
            }

            if (favTracks.size > 0) {
                state.postValue(
                    FavSongsFragmentScreenUpdate.DBFavoriteTracks(favTracks)
                )
            } else {
                state.postValue(
                    FavSongsFragmentScreenUpdate.ShowNoData
                )
            }
        }
    }


    fun getState(): LiveData<FavSongsFragmentScreenUpdate> {
        return state
    }
}

sealed class FavSongsFragmentScreenUpdate {

    object ShowNoData : FavSongsFragmentScreenUpdate()

    data class DBFavoriteTracks(
        val tracks: List<Track>
    ) : FavSongsFragmentScreenUpdate()
}



