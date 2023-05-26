package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val goBackButtonId = findViewById<FrameLayout>(R.id.settings_go_back_button)

        goBackButtonId.setOnClickListener{

            finish()

        }

    }
}