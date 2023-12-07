package com.example.playlistmaker.data.repositories



import android.media.MediaPlayer

import com.example.playlistmaker.domain.api.AudioRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioRepositoryImpl : AudioRepository {

    companion object{
        const val MEDIA_PLAYER_UPDATE_POS_PERIOD = 300L
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


    private var updatePos: Job? = null

    private fun scheduleFunUpdate() {
        updatePos = GlobalScope.launch {
            while (true) {
                delay(MEDIA_PLAYER_UPDATE_POS_PERIOD)
                updateFun()
            }
        }
    }

    private fun clearSchedule() {
        updatePos?.cancel()
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