package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButtonId   = findViewById<Button>(R.id.search_button)
        val mediaButtonId    = findViewById<Button>(R.id.medialibrary_button)
        val settingsButtonId = findViewById<Button>(R.id.settings_button)

        // search button
        val searchButtonClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "нажатие на клавишу \"Поиск\"", Toast.LENGTH_SHORT).show()
            }
        }
        searchButtonId.setOnClickListener(searchButtonClickListener)

        // media library
        mediaButtonId.setOnClickListener{
            Toast.makeText(this@MainActivity, "нажатие на клавишу \"Медиатека\"", Toast.LENGTH_SHORT).show()
        }

        // settings button
        settingsButtonId.setOnClickListener{
            Toast.makeText(this@MainActivity, "нажатие на клавишу \"Настройки\"", Toast.LENGTH_SHORT).show()
        }

    }
}