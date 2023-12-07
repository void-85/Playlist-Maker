package com.example.playlistmaker.ui.fragsHolderActivity.ui.settings


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.playlistmaker.domain.api.interactors.ThemeInteractor


class SettingsFragmentViewModel(
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

}
