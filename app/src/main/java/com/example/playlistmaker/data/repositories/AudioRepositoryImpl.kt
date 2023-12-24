package com.example.playlistmaker.data.repositories


import android.media.MediaPlayer

import com.example.playlistmaker.domain.api.repositories.AudioRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioRepositoryImpl : AudioRepository {

    companion object {
        const val MEDIA_PLAYER_UPDATE_POS_PERIOD_MILLIS = 300L
    }

    private var mediaPlayer = MediaPlayer()

    private enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    private var playerState = PlayerState.DEFAULT
    private lateinit var updateFun: () -> (Unit)
    private lateinit var onCompletionFun: () -> (Unit)
    private lateinit var onPlayFun: () -> (Unit)
    private lateinit var onPauseFun: () -> (Unit)


    private var updatePositionJob: Job? = null

    private fun scheduleFunUpdate() {
        updatePositionJob = GlobalScope.launch {
            while (true) {
                delay(MEDIA_PLAYER_UPDATE_POS_PERIOD_MILLIS)
                updateFun()
            }
        }
    }

    private fun clearSchedule() {
        updatePositionJob?.cancel()
    }


    override fun prepare(
        url: String,
        seekToWhenPrepared: Int,
        autoPlay: Boolean,
        updateFun: () -> (Unit),
        onCompletionFun: () -> (Unit),
        onPlayFun: () -> (Unit),
        onPauseFun: () -> (Unit)
    ) {
        this.updateFun = updateFun
        this.onCompletionFun = onCompletionFun
        this.onPlayFun = onPlayFun
        this.onPauseFun = onPauseFun

        mediaPlayer.reset()

        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {

            playerState = PlayerState.PREPARED
            mediaPlayer.seekTo(seekToWhenPrepared)
            this.updateFun()
            if (autoPlay) start()
        }

        mediaPlayer.setOnCompletionListener {

            playerState = PlayerState.PREPARED
            clearSchedule()
            this.onCompletionFun()
        }
    }

    override fun start() {
        scheduleFunUpdate()
        mediaPlayer.start()
        onPlayFun()
        playerState = PlayerState.PLAYING
    }

    override fun pause() {
        clearSchedule()
        mediaPlayer.pause()
        onPauseFun()
        playerState = PlayerState.PAUSED
    }

    override fun release() {
        clearSchedule()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }

    override fun isPlaying(): Boolean {
        return playerState == PlayerState.PLAYING
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }
}