package com.example.playlistmaker.domain.useCases



import com.example.playlistmaker.domain.api.AppPrefsRepository
import com.example.playlistmaker.domain.api.ThemeInteractor



class ThemeInteractorImpl(
    private val appPrefsRepositoryImpl: AppPrefsRepository,
    private val setThemeFun: (Boolean) -> Unit
) : ThemeInteractor {

    override fun isThemeDark(): Boolean {
        return appPrefsRepositoryImpl.isThemeDark()
    }

    override fun setTheme( darkThemeEnabled: Boolean) {
        setThemeFun( darkThemeEnabled )
        appPrefsRepositoryImpl.setDarkTheme(darkThemeEnabled)
    }
}