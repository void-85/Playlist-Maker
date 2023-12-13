package com.example.playlistmaker.ui.utils

sealed class Option<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Data<T>(
        data: T
    ) : Option<T>(data)

    class Error<T>(
        data: T? = null,
        error: String
    ) : Option<T>(data, error)
}