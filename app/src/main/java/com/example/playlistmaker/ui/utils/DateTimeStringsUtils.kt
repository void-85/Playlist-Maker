package com.example.playlistmaker.ui.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.millisToMinSec(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}

fun Int.toTrackNumberString():String{

    if( this in 5..20     ) return "${this} треков"
    if( this % 10 == 1    ) return "${this} трек"
    if( this % 10 in 2..4 ) return "${this} трека"

    return "${this} треков"
}
