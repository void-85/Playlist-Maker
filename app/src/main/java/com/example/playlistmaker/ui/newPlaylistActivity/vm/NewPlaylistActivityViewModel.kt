package com.example.playlistmaker.ui.newPlaylistActivity.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.newPlaylistActivity.act.NewPlaylistActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewPlaylistActivityViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    private var state = MutableLiveData<NewPlaylistActivityScreenUpdate>()
    fun getState(): LiveData<NewPlaylistActivityScreenUpdate> {
        return state
    }

    fun createOrEditPlaylist(
        playlist: Playlist,
    ) {
        viewModelScope.launch {

            var playlistExistsAndWasUpdated = false

            mediaInteractor.getAllPlaylistsWithoutTracksData().collect{
                if( it.id == playlist.id ){
                    mediaInteractor.updatePlaylistInfo(playlist)
                    playlistExistsAndWasUpdated = true

                    delay(NewPlaylistActivity.TIME_DELAY_FOR_DB_TRANSACTION)
                    state.postValue(NewPlaylistActivityScreenUpdate.NotifyUserPlaylistEdited)
                }
            }

            if(!playlistExistsAndWasUpdated){
                mediaInteractor.createPlaylist(playlist)

                delay(NewPlaylistActivity.TIME_DELAY_FOR_DB_TRANSACTION)
                state.postValue(NewPlaylistActivityScreenUpdate.NotifyUserPlaylistCreated)
            }
        }
    }
}

sealed class NewPlaylistActivityScreenUpdate {

    object NotifyUserPlaylistCreated : NewPlaylistActivityScreenUpdate()
    object NotifyUserPlaylistEdited : NewPlaylistActivityScreenUpdate()
}