package com.example.playlistmaker.data.level_3_repositories


import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

import com.example.playlistmaker.App
import com.example.playlistmaker.domain.level_1_entities.AudioRepository


class AudioRepositoryImpl :AudioRepository {

    private var mediaPlayer = MediaPlayer()

    companion object {
        private const val STATE_DEFAULT  = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING  = 2
        private const val STATE_PAUSED   = 3
    }

    private var playerState = STATE_DEFAULT
    private lateinit var updateFun       :() -> (Unit)
    private lateinit var onCompletionFun :() -> (Unit)
    private lateinit var onPlayFun       :() -> (Unit)
    private lateinit var onPauseFun      :() -> (Unit)




    private val handler = Handler( Looper.getMainLooper() )
    private val updatePosRunnable =
        Runnable {
            updateFun()
            scheduleFunUpdate()
        }

    private fun scheduleFunUpdate(){
        handler.postDelayed(updatePosRunnable, App.MEDIA_PLAYER_UPDATE_POS_PERIOD)
    }
    private fun clearSchedule(){
        handler.removeCallbacks(updatePosRunnable)
    }





    override fun prepare (
        url                :String       ,
        seekToWhenPrepared :Int          ,
        autoPlay           :Boolean      ,
        updateFun          :() -> (Unit) ,
        onCompletionFun    :() -> (Unit) ,
        onPlayFun          :() -> (Unit) ,
        onPauseFun         :() -> (Unit) )
    {

        this.updateFun       = updateFun
        this.onCompletionFun = onCompletionFun
        this.onPlayFun       = onPlayFun
        this.onPauseFun      = onPauseFun


        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()


        mediaPlayer.setOnPreparedListener {

            playerState = STATE_PREPARED
            mediaPlayer.seekTo( seekToWhenPrepared )
            this.updateFun()
            if( autoPlay ) start()
        }

        mediaPlayer.setOnCompletionListener {

            playerState = STATE_PREPARED
            clearSchedule()
            this.onCompletionFun()
        }

    }


    override fun start() {
        scheduleFunUpdate()
        mediaPlayer.start()
        onPlayFun()
        playerState = STATE_PLAYING
    }

    override fun pause() {
        clearSchedule()
        mediaPlayer.pause()
        onPauseFun()
        playerState = STATE_PAUSED
    }

    override fun release() {
        clearSchedule()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }

    override fun isPlaying(): Boolean {
        return playerState == STATE_PLAYING
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }
}