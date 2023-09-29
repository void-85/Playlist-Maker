package com.example.playlistmaker.domain



import com.example.playlistmaker.domain.api.IntentInteractor
import com.example.playlistmaker.domain.api.IntentRepository


class IntentInteractorImpl(
    private val intentRepository: IntentRepository
) : IntentInteractor {

    override fun sendIntent(intent: Any) {
        intentRepository.sendIntent(intent)
    }

    override fun sendIntentWithChooser(intent: Any) {
        intentRepository.sendIntentWithChooser(intent)
    }
}