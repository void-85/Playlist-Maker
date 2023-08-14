package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale


fun Long.millisToMinSec(): String{
    return SimpleDateFormat("m:ss", Locale.getDefault() ).format(this)
}
