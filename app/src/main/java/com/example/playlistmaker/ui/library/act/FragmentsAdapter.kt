package com.example.playlistmaker.ui.library.act


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class FragmentsAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0    -> FavSongsFragment.newInstance("Ваша медиатека пуста")
            else -> PlaylistsFragment.newInstance("Вы не создали\nни одного плейлиста")
        }
    }

    override fun getItemCount(): Int = 2
}

