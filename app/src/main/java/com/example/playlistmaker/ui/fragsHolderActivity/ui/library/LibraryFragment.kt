package com.example.playlistmaker.ui.fragsHolderActivity.ui.library


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator


class LibraryFragment : Fragment() {


    private lateinit var binding: FragmentLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)*/

        binding.mediaLibraryViewPager.adapter =
            FragmentsAdapter(
                requireActivity().supportFragmentManager,
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

        //binding.mediaLibraryBackButton.setOnClickListener { finish() }
        //setSupportActionBar( binding.mediaLibraryToolbar )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}









