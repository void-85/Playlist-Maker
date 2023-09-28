package com.example.playlistmaker.domain.api



interface IntentRepository {

    fun sendIntent( intent: Any)
    fun sendIntentWithChooser( intent: Any)
}