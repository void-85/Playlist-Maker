package com.example.playlistmaker.ui.player.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.playlistmaker.domain.api.interactors.MediaInteractor
import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.launch


class MediaActivityViewModel(
    private val mediaInteractor: MediaInteractor
) : ViewModel() {

    private var screenData =
        MutableLiveData<MediaActivityScreenUpdate>(
            MediaActivityScreenUpdate.AllData(
                0,
                "",
                "-",
                "-",
                "-",
                "-",
                "_",
                "-",
                "-",
                true,
                false
            )
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

            var isTrackFavorite = false
            viewModelScope.launch {
                isTrackFavorite = mediaInteractor.isTrackFavorite(track.trackId)
            }

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
                    showPlayElsePauseButtonState = true,
                    trackIsFavorite = isTrackFavorite
                )
            )
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

        if (mediaInteractor.getCurrentPosition() > 0L) {
            val track: Track? = mediaInteractor.getCurrentlyPlaying()
            if (track is Track) {

                var isTrackFavorite = false
                viewModelScope.launch {
                    isTrackFavorite = mediaInteractor.isTrackFavorite(track.trackId)
                }

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
                        showPlayElsePauseButtonState = !mediaInteractor.isMediaPlayerToResumeOnCreate(),
                        trackIsFavorite = isTrackFavorite
                    )
                )
            }
        }

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

    fun favoriteTrackButtonPressed( makeTrackFavorite:Boolean ){

        val track: Track? = mediaInteractor.getCurrentlyPlaying()
        if (track is Track) {
            viewModelScope.launch {
                if (makeTrackFavorite)
                    mediaInteractor.insertTrack(track)
                else
                    mediaInteractor.deleteTrack(track)
            }
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
        val trackIsFavorite: Boolean
    ) : MediaActivityScreenUpdate()

    object PlayFinished : MediaActivityScreenUpdate()

    object DoNothing : MediaActivityScreenUpdate()
}