package com.example.playlistmaker.data.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.millisToMinSec(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}
