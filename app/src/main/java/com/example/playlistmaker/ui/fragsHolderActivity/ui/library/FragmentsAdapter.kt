package com.example.playlistmaker.ui.fragsHolderActivity.ui.library


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments.FavSongsFragment
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments.PlaylistsFragment


class FragmentsAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0    -> FavSongsFragment .newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

    override fun getItemCount(): Int = 2
}

