package com.example.playlistmaker.ui.main.act



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.intentInteractor
import com.example.playlistmaker.ui.player.MediaActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.act.SettingsActivity



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.searchButton.setOnClickListener {

            val searchIntent = Intent(this, SearchActivity::class.java)
            intentInteractor.sendIntent(searchIntent)
            //startActivity(searchIntent)
        }

        binding.medialibraryButton.setOnClickListener {

            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        binding.settingsButton.setOnClickListener {

            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}