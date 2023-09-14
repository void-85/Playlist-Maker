package com.example.playlistmaker.domain.level_2_use_cases

import com.example.playlistmaker.domain.level_1_entities.AppPrefsRepository
import com.example.playlistmaker.domain.level_1_entities.Interactor

class InteractorImpl( private val appPrefsRepositoryImpl: AppPrefsRepository) :Interactor {

    override fun isThemeDark(): Boolean {
        return appPrefsRepositoryImpl.isThemeDark()
    }
    override fun setDarkTheme(darkThemeEnabled: Boolean) {
        appPrefsRepositoryImpl.setDarkTheme(darkThemeEnabled)
    }


    override fun getSearchHistory(): String {
        return appPrefsRepositoryImpl.getSearchHistory()
    }
    override fun setSearchHistory(text: String) {
        appPrefsRepositoryImpl.setSearchHistory(text)
    }


    override fun isSearchHistoryEmpty(): Boolean {
        return appPrefsRepositoryImpl.isSearchHistoryEmpty()
    }
    override fun setSearchHistoryEmpty(isEmpty: Boolean) {
        appPrefsRepositoryImpl.setSearchHistoryEmpty(isEmpty)
    }


    override fun getCurrentlyPlaying(): String {
        return appPrefsRepositoryImpl.getCurrentlyPlaying()
    }
    override fun setCurrentlyPlaying(text: String) {
        appPrefsRepositoryImpl.setCurrentlyPlaying(text)
    }


    override fun getMediaPlayerLastPosition(): Long {
        return appPrefsRepositoryImpl.getMediaPlayerLastPosition()
    }
    override fun setMediaPlayerLastPosition(position: Long) {
        appPrefsRepositoryImpl.setMediaPlayerLastPosition(position)
    }


    override fun isMediaPlayerToResumeOnCreate(): Boolean {
        return appPrefsRepositoryImpl.isMediaPlayerToResumeOnCreate()
    }
    override fun setMediaPlayerToResumeOnCreate(resume: Boolean) {
        appPrefsRepositoryImpl.setMediaPlayerToResumeOnCreate(resume)
    }
}