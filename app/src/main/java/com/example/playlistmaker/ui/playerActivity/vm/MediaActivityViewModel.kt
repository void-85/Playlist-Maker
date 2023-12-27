package com.example.playlistmaker.ui.playerActivity.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track


class MediaActivityViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    private var screenData =
        MutableLiveData<MediaActivityScreenUpdate>(
            MediaActivityScreenUpdate.DoNothing
        )

    init {

        val track: Track? = mediaInteractor.getCurrentlyPlaying()
        if (track is Track) {

            mediaInteractor.prepare(
                track.previewUrl,
                mediaInteractor.getMediaPlayerLastPosition().toInt(),
                mediaInteractor.isMediaPlayerToResumeOnCreate(),
                ::updateFun,
                ::onCompletionFun,
                ::onPlayFun,
                ::onPauseFun
            )

            viewModelScope.launch {

                val dbPlaylists = ArrayList<Playlist>()
                mediaInteractor.getAllPlaylists().collect { dbPlaylists.add(it) }

                screenData.postValue(
                    MediaActivityScreenUpdate.AllData(
                        timeCode = 0L,
                        artworkUrl100 = track.artworkUrl100,
                        mediaTitle = track.trackName,
                        mediaArtist = track.artistName,
                        mediaLength = track.trackTime,
                        mediaAlbum = track.collectionName,
                        mediaDate = track.releaseDate,
                        mediaGenre = track.primaryGenreName,
                        mediaCountry = track.country,
                        showPlayElsePauseButtonState = !mediaInteractor.isPlaying(),
                        trackIsFavorite = mediaInteractor.isTrackFavorite(track),
                        //trackIsFavorite = track.isFavorite

                        dbPlaylists
                    )
                )
            }
        }
    }

    private fun onPlayFun() {
        screenData.postValue(
            MediaActivityScreenUpdate.ShowPlayElsePauseButtonStateOnly(false)
        )
    }

    private fun onPauseFun() {
        screenData.postValue(
            MediaActivityScreenUpdate.ShowPlayElsePauseButtonStateOnly(true)
        )
    }

    private fun onCompletionFun() {
        screenData.postValue(
            MediaActivityScreenUpdate.PlayFinished
        )
    }

    private fun updateFun() {
        screenData.postValue(
            MediaActivityScreenUpdate.TimeCodeOnly(
                mediaInteractor.getCurrentPosition()
            )
        )
    }


    fun getState(): LiveData<MediaActivityScreenUpdate> {
        return screenData
    }

    fun onStartActivity() {
        if (mediaInteractor.isMediaPlayerToResumeOnCreate()) {
            mediaInteractor.start()
        }

        /*        var playlistsUpdated = false
                if (mediaInteractor.getCurrentPosition() >= 0L) {*/


        val track: Track? = mediaInteractor.getCurrentlyPlaying()
        if (track is Track) {

            viewModelScope.launch {

                val dbPlaylists = ArrayList<Playlist>()
                mediaInteractor.getAllPlaylists().collect { dbPlaylists.add(it) }
                //playlistsUpdated = true

                screenData.postValue(
                    MediaActivityScreenUpdate.AllData(
                        timeCode = mediaInteractor.getCurrentPosition(),
                        artworkUrl100 = track.artworkUrl100,
                        mediaTitle = track.trackName,
                        mediaArtist = track.artistName,
                        mediaLength = track.trackTime,
                        mediaAlbum = track.collectionName,
                        mediaDate = track.releaseDate,
                        mediaGenre = track.primaryGenreName,
                        mediaCountry = track.country,
                        //showPlayElsePauseButtonState = !mediaInteractor.isMediaPlayerToResumeOnCreate(),
                        showPlayElsePauseButtonState = !mediaInteractor.isPlaying(),
                        trackIsFavorite = false,

                        playlists = dbPlaylists
                    )
                )

                delay(200)

                screenData.postValue(
                    MediaActivityScreenUpdate.ShowTrackIsFavorite(
                        mediaInteractor.isTrackFavorite(track)
                    )
                )
            }
        }
        /* }

         if (!playlistsUpdated) {

             viewModelScope.launch {
                 val dbPlaylists = ArrayList<Playlist>()
                 mediaInteractor.getAllPlaylists().collect { dbPlaylists.add(it) }

                 screenData.postValue(
                     MediaActivityScreenUpdate.BottomSheetPlaylistsOnly(dbPlaylists)
                 )
             }
         }*/
    }

    fun onStopActivity() {
        mediaInteractor.setMediaPlayerToResumeOnCreate(mediaInteractor.isPlaying())
        if (mediaInteractor.isPlaying()) mediaInteractor.pause()
    }

    fun pause() {
        mediaInteractor.pause()
    }

    fun release() {
        mediaInteractor.release()
    }

    fun setMediaPlayerLastPositionToStart() {
        mediaInteractor.setMediaPlayerLastPosition(0L)
        mediaInteractor.setMediaPlayerToResumeOnCreate(false)
    }

    fun setMediaPlayerLastPosition() {
        mediaInteractor.setMediaPlayerLastPosition(mediaInteractor.getCurrentPosition())
    }

    fun playPauseButtonPressed() {
        if (mediaInteractor.isPlaying()) {
            mediaInteractor.pause()
        } else {
            if (mediaInteractor.getCurrentlyPlaying() is Track) {
                //Log.d("<!>",mediaInteractor.getCurrentlyPlaying().toString())
                mediaInteractor.start()
            }
        }
    }

    fun favoriteTrackButtonPressed(makeTrackFavorite: Boolean) {

        val track: Track? = mediaInteractor.getCurrentlyPlaying()
        if (track is Track) {
            viewModelScope.launch {
                if (makeTrackFavorite)
                    mediaInteractor.insertTrack(track)
                else
                    mediaInteractor.deleteTrack(track)

                screenData.postValue(
                    MediaActivityScreenUpdate.ShowTrackIsFavorite(
                        mediaInteractor.isTrackFavorite(track)
                    )
                )
            }
        }
    }

    fun dataWasRecieved() {
        screenData.postValue(
            MediaActivityScreenUpdate.DoNothing
        )
    }

    fun includeOrExcludeCurrentTrackInFromPlaylist(playlist: Playlist) {
        val track: Track? = mediaInteractor.getCurrentlyPlaying()
        if (track is Track) {

            val resultMessage: String

            if (playlist.tracks.contains(track)) {

                resultMessage = "Трек уже добавлен в плейлист \"${playlist.name}\""

            } else {

                resultMessage = "Добавлено в плейлист \"${playlist.name}\""

                viewModelScope.launch {

                    mediaInteractor.deletePlaylist(playlist.id)

                    val newTracksList = ArrayList<Track>()
                    newTracksList.addAll(playlist.tracks)
                    newTracksList.add(track)

                    mediaInteractor.createPlaylist(
                        Playlist(
                            id = 0,

                            name = playlist.name,
                            description = playlist.description,

                            imageId = playlist.imageId,

                            tracks = newTracksList,
                            numberOfTracks = playlist.numberOfTracks + 1
                        )
                    )

                    onStartActivity()
                }
            }

            screenData.postValue(
                MediaActivityScreenUpdate.NotifyUser(resultMessage)
            )
        }
    }
}


sealed class MediaActivityScreenUpdate {

    data class TimeCodeOnly(
        val timeCode: Long
    ) : MediaActivityScreenUpdate()

    data class ShowPlayElsePauseButtonStateOnly(
        val state: Boolean
    ) : MediaActivityScreenUpdate()

    data class ShowTrackIsFavorite(
        val trackIsFavorite: Boolean
    ) : MediaActivityScreenUpdate()

    data class BottomSheetPlaylistsOnly(
        val playlists: List<Playlist>
    ) : MediaActivityScreenUpdate()

    data class NotifyUser(
        val message: String
    ) : MediaActivityScreenUpdate()

    data class AllData(
        val timeCode: Long,
        val artworkUrl100: String,
        val mediaTitle: String,
        val mediaArtist: String,
        val mediaLength: String,
        val mediaAlbum: String,
        val mediaDate: String,
        val mediaGenre: String,
        val mediaCountry: String,
        val showPlayElsePauseButtonState: Boolean,
        val trackIsFavorite: Boolean,

        val playlists: List<Playlist>
    ) : MediaActivityScreenUpdate()

    object PlayFinished : MediaActivityScreenUpdate()

    object DoNothing : MediaActivityScreenUpdate()
}