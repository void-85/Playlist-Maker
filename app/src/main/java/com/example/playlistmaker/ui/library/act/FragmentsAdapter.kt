package com.example.playlistmaker.ui.library.act


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.example.playlistmaker.R
import com.example.playlistmaker.ui.library.act.fragments.FavSongsFragment
import com.example.playlistmaker.ui.library.act.fragments.PlaylistsFragment


class FragmentsAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0    -> FavSongsFragment .newInstance( R.string.media_library_fav_songs_tab_the_library_is_empty )
            else -> PlaylistsFragment.newInstance( R.string.media_library_playlists_tab_no_playlists )
        }
    }

    override fun getItemCount(): Int = 2
}

