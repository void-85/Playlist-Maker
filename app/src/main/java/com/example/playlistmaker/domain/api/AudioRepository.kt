package com.example.playlistmaker.domain.api



interface AudioRepository {

    fun getCurrentPosition() :Long
    fun isPlaying() :Boolean

    fun prepare (
        url                :String       ,
        seekToWhenPrepared :Int          ,
        autoPlay           :Boolean      ,
        updateFun          :() -> (Unit) ,
        onCompletionFun    :() -> (Unit) ,
        onPlayFun          :() -> (Unit) ,
        onPauseFun         :() -> (Unit) )

    fun start()
    fun pause()
    fun release()
}