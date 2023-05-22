package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val goBackButtonId = findViewById<ImageView>(R.id.settings_go_back_button)

        goBackButtonId.setOnClickListener{

            val mainScreenIntent = Intent(this, MainActivity::class.java)
            startActivity(mainScreenIntent)

        }

    }
}