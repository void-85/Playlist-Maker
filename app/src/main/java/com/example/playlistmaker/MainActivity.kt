package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButtonId   = findViewById<Button>(R.id.search_button)
        val mediaButtonId    = findViewById<Button>(R.id.medialibrary_button)
        val settingsButtonId = findViewById<Button>(R.id.settings_button)



        // search button

        /*val searchButtonClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "нажатие на клавишу \"Поиск\"", Toast.LENGTH_SHORT).show()
            }
        }
        searchButtonId.setOnClickListener(searchButtonClickListener)*/

        searchButtonId.setOnClickListener{
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }



        // media library
        mediaButtonId.setOnClickListener{

            //Toast.makeText(this@MainActivity, "нажатие на клавишу \"Медиатека\"", Toast.LENGTH_SHORT).show()

            val mediaIntent = Intent( this, MediaActivity::class.java )
            startActivity(mediaIntent)
        }



        // settings button
        settingsButtonId.setOnClickListener{

            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)

        }

    }
}