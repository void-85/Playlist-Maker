package com.example.playlistmaker.data.repositories



import android.app.Activity
import android.content.Context
import android.content.Intent

import com.example.playlistmaker.domain.api.IntentRepository



class IntentRepositoryImpl(
    private val context: Context
) : IntentRepository {

    override fun sendIntent(intent: Any) {
        if (intent is Intent) {
            context.startActivity(
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    override fun sendIntentWithChooser(intent: Any) {
        if (intent is Intent) {
            context.startActivity(
                Intent.createChooser(
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    null
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }
}