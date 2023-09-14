package com.example.playlistmaker.domain.level_2_use_cases

import com.example.playlistmaker.domain.level_1_entities.AppPrefsRepository
import com.example.playlistmaker.domain.level_1_entities.Interactor

class InteractorImpl( private val appPrefsRepositoryImpl: AppPrefsRepository) :Interactor {

    override fun isThemeDark() :Boolean {
        return appPrefsRepositoryImpl.isThemeDark()
    }

    override fun setDarkTheme(darkThemeEnabled: Boolean) {
        appPrefsRepositoryImpl.setDarkTheme( darkThemeEnabled )
    }

}