package com.example.playlistmaker.domain.settings


import android.util.Log

import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.ThemeInteractor


class ThemeInteractorImpl(
    private val appPrefsRepositoryImpl: AppPrefsRepository
) : ThemeInteractor {

    private var setThemeFun: (Boolean) -> Unit = { _ ->
        Log.d("<!>", "NOT INIT FUN CALLED!")
    }

    override fun setThemeSwitchFun(themeSwitchFun: (Boolean) -> Unit) {
        setThemeFun = themeSwitchFun
    }

    override fun isThemeDark(): Boolean {
        return appPrefsRepositoryImpl.isThemeDark()
    }

    override fun applyCurrentTheme() {
        setThemeFun( isThemeDark() )
    }

    override fun setTheme(darkThemeEnabled: Boolean) {
        setThemeFun(darkThemeEnabled)
        appPrefsRepositoryImpl.setDarkTheme(darkThemeEnabled)
    }
}