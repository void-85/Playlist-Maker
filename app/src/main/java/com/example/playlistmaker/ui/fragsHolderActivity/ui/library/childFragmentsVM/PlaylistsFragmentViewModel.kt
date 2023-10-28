package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class PlaylistsFragmentViewModel : ViewModel() {

    private var state =
        MutableLiveData<PlaylistsFragmentScreenUpdate>(
            PlaylistsFragmentScreenUpdate.ShowNoData
        )

    init {
        state.postValue(PlaylistsFragmentScreenUpdate.ShowNoData)
    }


    fun getState(): LiveData<PlaylistsFragmentScreenUpdate> {
        return state
    }
}

sealed class PlaylistsFragmentScreenUpdate {
    object ShowNoData : PlaylistsFragmentScreenUpdate()
}