package com.example.playlistmaker.ui.newPlaylistActivity.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.entities.Playlist
import kotlinx.coroutines.launch

class NewPlaylistActivityViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch { mediaInteractor.createPlaylist(playlist) }
    }

}