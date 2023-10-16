package com.example.playlistmaker.ui.library.act


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator


class Library : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mediaLibraryViewPager.adapter =
            FragmentsAdapter(
                supportFragmentManager,
                lifecycle
            )

        tabMediator = TabLayoutMediator(
            binding.mediaLibraryTabs,
            binding.mediaLibraryViewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.media_library_tab_1_text)
                1 -> tab.text = getString(R.string.media_library_tab_2_text)
            }
        }
        tabMediator.attach()

        binding.mediaLibraryBackButton.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}


