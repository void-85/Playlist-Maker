package com.example.playlistmaker.ui.main.act



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.player.MediaActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.act.SettingsActivity

import com.example.playlistmaker.ui.main.vm.MainActivityViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(
            this,
            MainActivityViewModel.getViewModelFactory()
        )[MainActivityViewModel::class.java]


        binding.searchButton.setOnClickListener {

            val searchIntent = Intent(this, SearchActivity::class.java)
            viewModel.sendIntent(searchIntent)
        }

        binding.medialibraryButton.setOnClickListener {

            val mediaIntent = Intent(this, MediaActivity::class.java)
            viewModel.sendIntent(mediaIntent)
        }

        binding.settingsButton.setOnClickListener {

            val settingsIntent = Intent(this, SettingsActivity::class.java)
            viewModel.sendIntent(settingsIntent)
        }
    }
}