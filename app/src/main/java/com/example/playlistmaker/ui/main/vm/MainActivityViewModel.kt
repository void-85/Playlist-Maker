package com.example.playlistmaker.ui.main.vm


import androidx.lifecycle.ViewModel

import com.example.playlistmaker.domain.api.ThemeInteractor


class MainActivityViewModel(
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

    /*companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainActivityViewModel(
                        Creator.provideThemeInteractor()
                    )
                }
            }
    }*/
}
