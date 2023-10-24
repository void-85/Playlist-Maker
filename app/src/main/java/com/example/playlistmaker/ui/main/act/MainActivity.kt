package com.example.playlistmaker.ui.main.act


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate

import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.LibraryFragment
import com.example.playlistmaker.ui.search.act.SearchActivity
import com.example.playlistmaker.ui.fragsHolderActivity.ui.settings.SettingsFragment
import com.example.playlistmaker.ui.main.vm.MainActivityViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainActivityViewModel>()

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

        viewModel.setThemeSwitchFun( setTheme )
        viewModel.applyCurrentTheme()


        binding.searchButton.setOnClickListener {

            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding.medialibraryButton.setOnClickListener {

            val mediaIntent = Intent(this, LibraryFragment::class.java)
            startActivity(mediaIntent)
        }

        binding.settingsButton.setOnClickListener {

            val settingsIntent = Intent(this, SettingsFragment::class.java)
            startActivity(settingsIntent)
        }
    }
}