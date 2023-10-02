package com.example.playlistmaker.ui.main.act


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider

import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.player.act.MediaActivity
import com.example.playlistmaker.ui.search.act.SearchActivity
import com.example.playlistmaker.ui.settings.act.SettingsActivity

import com.example.playlistmaker.ui.main.vm.MainActivityViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel


    private val setTheme: ((Boolean) -> Unit) = { darkThemeEnabled ->
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(
            this,
            MainActivityViewModel.getViewModelFactory()
        )[MainActivityViewModel::class.java]
        viewModel.setThemeSwitchFun( setTheme )
        viewModel.applyCurrentTheme()


        binding.searchButton.setOnClickListener {

            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
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