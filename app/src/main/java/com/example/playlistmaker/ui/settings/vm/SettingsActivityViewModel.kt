package com.example.playlistmaker.ui.settings.vm



import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.playlistmaker.domain.api.IntentInteractor
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.intentInteractor
import com.example.playlistmaker.themeInteractor



class SettingsActivityViewModel (
    private val themeInteractor: ThemeInteractor,
    private val intentInteractor: IntentInteractor
):ViewModel() {

    private var switchedToDarkTheme = MutableLiveData(false)

    init {
        switchedToDarkTheme.postValue(themeInteractor.isThemeDark())
    }

    fun getSwitchToDarkThemeState(): LiveData<Boolean> = switchedToDarkTheme

    fun setSwitchToDarkThemeState(darkThemeEnabled: Boolean) {
        switchedToDarkTheme.postValue(darkThemeEnabled)
        themeInteractor.setTheme(darkThemeEnabled)
    }

    fun sendIntentWithChooser(intent: Intent) {
        intentInteractor.sendIntentWithChooser(intent)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsActivityViewModel(
                    themeInteractor,
                    intentInteractor
                )
            }
        }
    }
}
