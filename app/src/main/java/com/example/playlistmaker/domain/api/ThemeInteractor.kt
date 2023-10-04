package com.example.playlistmaker.domain.api


interface ThemeInteractor {

    fun isThemeDark(): Boolean

    fun setThemeSwitchFun( themeSwitchFun: (Boolean)->Unit )
    fun setTheme(darkThemeEnabled: Boolean)

    fun applyCurrentTheme()
}