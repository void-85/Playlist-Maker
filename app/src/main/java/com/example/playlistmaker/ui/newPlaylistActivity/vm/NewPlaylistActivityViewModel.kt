package com.example.playlistmaker.ui.newPlaylistActivity.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.entities.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewPlaylistActivityViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    fun createPlaylist(
        deletePlaylistById: Long?,
        createPlaylist: Playlist,
    ) {
        viewModelScope.launch {

            if (deletePlaylistById is Long) {
                mediaInteractor.deletePlaylist(deletePlaylistById)
            }
            mediaInteractor.createPlaylist(createPlaylist)
        }
    }


}