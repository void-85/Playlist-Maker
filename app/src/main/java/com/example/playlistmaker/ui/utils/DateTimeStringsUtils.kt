package com.example.playlistmaker.ui.utils

import android.content.Context
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

fun Long.millisToMinSec(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}

fun Int.toTrackAmountString(context: Context):String{

    if( this in 5..20     ) return "${this} "+context.getString(R.string.tracks_amount_string_form_c)//треков"
    if( this % 10 == 1    ) return "${this} "+context.getString(R.string.tracks_amount_string_form_a)//трек"
    if( this % 10 in 2..4 ) return "${this} "+context.getString(R.string.tracks_amount_string_form_b)//трека"

    return "${this} "+context.getString(R.string.tracks_amount_string_form_c)//треков"
}

fun Int.toMinutesAmountString(context: Context):String{

    if( this in 5..20     ) return "${this} "+context.getString(R.string.minutes_amount_string_form_c)//треков"
    if( this % 10 == 1    ) return "${this} "+context.getString(R.string.minutes_amount_string_form_a)//трек"
    if( this % 10 in 2..4 ) return "${this} "+context.getString(R.string.minutes_amount_string_form_b)//трека"

    return "${this} "+context.getString(R.string.minutes_amount_string_form_c)//треков"
}