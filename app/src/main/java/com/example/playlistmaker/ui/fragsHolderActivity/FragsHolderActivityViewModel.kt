package com.example.playlistmaker.ui.fragsHolderActivity


import androidx.lifecycle.ViewModel

import com.example.playlistmaker.domain.api.interactors.ThemeInteractor


class FragsHolderActivityViewModel(
    private val themeInteractor: ThemeInteractor
) : ViewModel() {

    init {

    }

    fun setThemeSwitchFun( setThemeFun : ((Boolean) -> Unit) ){
        themeInteractor.setThemeSwitchFun(setThemeFun)
    }

    fun applyCurrentTheme(){
        themeInteractor.applyCurrentTheme()
    }
}
