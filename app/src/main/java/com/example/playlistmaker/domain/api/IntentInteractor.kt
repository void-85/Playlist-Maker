package com.example.playlistmaker.domain.api



interface IntentInteractor {

    fun sendIntent( intent: Any)
    fun sendIntentWithChooser( intent: Any)
}