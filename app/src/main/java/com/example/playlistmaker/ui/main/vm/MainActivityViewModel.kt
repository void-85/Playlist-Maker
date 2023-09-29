package com.example.playlistmaker.ui.main.vm



import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.playlistmaker.domain.api.IntentInteractor
import com.example.playlistmaker.intentInteractor



class MainActivityViewModel(
    private val intentInteractor: IntentInteractor
) : ViewModel() {

    init {

    }

    fun sendIntent(intent: Intent) {
        intentInteractor.sendIntent(intent)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainActivityViewModel(intentInteractor)
                }
            }
    }
}
