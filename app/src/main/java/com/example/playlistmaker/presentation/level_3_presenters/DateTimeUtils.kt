package com.example.playlistmaker.presentation.level_3_presenters

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.millisToMinSec(): String{
    return SimpleDateFormat("mm:ss", Locale.getDefault() ).format(this)
}
