package com.example.playlistmaker.ui.player.vm




import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.mediaInteractor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



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
                true
            )
        )

    init {

        val json: String = mediaInteractor.getCurrentlyPlaying()
        if (json.isNotEmpty()) {

            val data = Gson().fromJson<Track>(
                json,
                object : TypeToken<Track>() {}.type
            )

            mediaInteractor.prepare (
                data.previewUrl   ,
                mediaInteractor.getMediaPlayerLastPosition().toInt() ,
                mediaInteractor.isMediaPlayerToResumeOnCreate()      ,
                ::updateFun       ,
                ::onCompletionFun ,
                ::onPlayFun       ,
                ::onPauseFun      )

            screenData.postValue(
                MediaActivityScreenUpdate.AllData(
                    timeCode = 0L                    ,
                    artworkUrl100 = data.artworkUrl100    ,
                    mediaTitle    = data.trackName        ,
                    mediaArtist   = data.artistName       ,
                    mediaLength   = data.trackTime        ,
                    mediaAlbum    = data.collectionName   ,
                    mediaDate     = data.releaseDate      ,
                    mediaGenre    = data.primaryGenreName ,
                    mediaCountry  = data.country          ,
                    showPlayElsePauseButtonState = true
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
            MediaActivityScreenUpdate.ShowPlayElsePauseButtonStateOnly(true)
        )
        screenData.postValue(
            MediaActivityScreenUpdate.TimeCodeOnly(0)
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

    fun onStartActivity(){
        if (mediaInteractor.isMediaPlayerToResumeOnCreate()) {
            mediaInteractor.start()
        }
    }

    fun onStopActivity(){
        mediaInteractor.setMediaPlayerToResumeOnCreate( mediaInteractor.isPlaying() )
        mediaInteractor.pause()
    }

    fun pause(){
        mediaInteractor.pause()
    }

    fun release(){
        mediaInteractor.release()
    }

    fun setMediaPlayerLastPositionToStart() {
        mediaInteractor.setMediaPlayerLastPosition(0L)
        mediaInteractor.setMediaPlayerToResumeOnCreate(false)
    }

    fun setMediaPlayerLastPosition() {
        mediaInteractor.setMediaPlayerLastPosition( mediaInteractor.getCurrentPosition() )
    }

    fun playPauseButtonPressed(){
        if (mediaInteractor.isPlaying()) {
            mediaInteractor.pause()
        } else {
            if (mediaInteractor.getCurrentlyPlaying().isNotEmpty()) {
                mediaInteractor.start()
            }
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MediaActivityViewModel(mediaInteractor)
                }
            }
    }
}



sealed class MediaActivityScreenUpdate {

    data class TimeCodeOnly(
        val timeCode      :Long
    ) : MediaActivityScreenUpdate()

    data class ShowPlayElsePauseButtonStateOnly(
        val state :Boolean
    ) :MediaActivityScreenUpdate()

    data class AllData(
        val timeCode      :Long,
        val artworkUrl100 :String,
        val mediaTitle    :String,
        val mediaArtist   :String,
        val mediaLength   :String,
        val mediaAlbum    :String,
        val mediaDate     :String,
        val mediaGenre    :String,
        val mediaCountry  :String,
        val showPlayElsePauseButtonState :Boolean
    ) : MediaActivityScreenUpdate()
}