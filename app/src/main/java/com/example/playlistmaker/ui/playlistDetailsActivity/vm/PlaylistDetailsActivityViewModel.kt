package com.example.playlistmaker.ui.playlistDetailsActivity.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.api.interactors.SearchInteractor
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.launch

class PlaylistDetailsActivityViewModel(
    private val mediaInteractor: MediaInteractor,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var updateData = MutableLiveData<PlaylistDetailsActivityScreenUpdate>()
    fun getState(): LiveData<PlaylistDetailsActivityScreenUpdate>{ return updateData }

    fun playTrack(track: Track){
       searchInteractor.setCurrentlyPlaying(track)
    }

    fun refreshPlaylistData(
        playlistId: Long
    ) {
        viewModelScope.launch {

            updateData.postValue(
                PlaylistDetailsActivityScreenUpdate.PlaylistDataRefreshed(
                    mediaInteractor.getPlaylist(playlistId)
                )
            )
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            mediaInteractor.deletePlaylist(playlistId)
        }
    }

    fun deleteTrackFromPlaylist(track: Track, playlist:Playlist) {

        viewModelScope.launch {

            mediaInteractor.deletePlaylist(playlist.id)

            val newTracksList = ArrayList<Track>()
            newTracksList.addAll(playlist.tracks)
            newTracksList.remove(track)

            mediaInteractor.createPlaylist(
                Playlist(
                    id = playlist.id,

                    name = playlist.name,
                    description = playlist.description,

                    imageId = playlist.imageId,

                    tracks = newTracksList,
                    amountOfTracks = playlist.amountOfTracks - 1
                )
            )

            updateData.postValue(
                PlaylistDetailsActivityScreenUpdate.PlaylistDataRefreshed(
                    mediaInteractor.getPlaylist(playlist.id)
                )
            )
        }
    }
}

sealed class PlaylistDetailsActivityScreenUpdate {

    data class PlaylistDataRefreshed(
        val playlist: Playlist
    ) : PlaylistDetailsActivityScreenUpdate()

}