package com.example.playlistmaker.ui.library.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FavSongsFragmentViewModel : ViewModel() {

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



