package com.example.playlistmaker.ui.settings.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator

import com.example.playlistmaker.domain.api.ThemeInteractor


class SettingsActivityViewModel(
    private val themeInteractor: ThemeInteractor
) : ViewModel() {

    private var switchedToDarkTheme = MutableLiveData(false)

    init {
        switchedToDarkTheme.postValue(themeInteractor.isThemeDark())
    }

    fun setThemeSwitchFun( setThemeFun : ((Boolean) -> Unit) ){
        themeInteractor.setThemeSwitchFun(setThemeFun)
    }

    fun getSwitchToDarkThemeState(): LiveData<Boolean> = switchedToDarkTheme

    fun setSwitchToDarkThemeState(darkThemeEnabled: Boolean) {
        switchedToDarkTheme.postValue(darkThemeEnabled)
        themeInteractor.setTheme(darkThemeEnabled)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsActivityViewModel( Creator.provideThemeInteractor())
            }
        }
    }
}