package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.entities.Playlist
import kotlinx.coroutines.launch


class PlaylistsFragmentViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    init {
        requestAllPlaylists()
    }

    private var state = MutableLiveData<PlaylistsFragmentScreenUpdate>()
    fun getState(): LiveData<PlaylistsFragmentScreenUpdate> {
        return state
    }

    fun requestAllPlaylists() {
        viewModelScope.launch {

            val playlists = ArrayList<Playlist>()
            mediaInteractor.getAllPlaylistsWithoutTracksData().collect {
                playlists.add(it)
            }
            //mediaInteractor.getAllPlaylists().collect { playlists.add(it) }
            state.postValue(PlaylistsFragmentScreenUpdate.ShowAllPlaylists(playlists))
        }
    }
}

sealed class PlaylistsFragmentScreenUpdate {

    //object ShowNoData : PlaylistsFragmentScreenUpdate()

    data class ShowAllPlaylists(
        val playlists: List<Playlist>
    ) : PlaylistsFragmentScreenUpdate()
}