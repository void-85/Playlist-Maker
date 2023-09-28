package com.example.playlistmaker.domain.api



interface ThemeInteractor {

    fun isThemeDark(): Boolean
    fun setTheme( darkThemeEnabled: Boolean)
}