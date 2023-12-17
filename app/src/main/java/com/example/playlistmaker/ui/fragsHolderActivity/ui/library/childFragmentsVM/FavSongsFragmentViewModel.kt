package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.interactors.SearchInteractor


class FavSongsFragmentViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var state =
        MutableLiveData<FavSongsFragmentScreenUpdate>(
            FavSongsFragmentScreenUpdate.ShowNoData
        )

    init {
        state.postValue(FavSongsFragmentScreenUpdate.ShowNoData)
    }


    fun getState(): LiveData<FavSongsFragmentScreenUpdate> {
        return state
    }
}

sealed class FavSongsFragmentScreenUpdate {
    object ShowNoData : FavSongsFragmentScreenUpdate()
}



