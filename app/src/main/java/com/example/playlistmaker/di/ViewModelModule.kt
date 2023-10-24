package com.example.playlistmaker.di


import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.FavSongsFragmentViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.FragsHolderActivityViewModel
import com.example.playlistmaker.ui.player.vm.MediaActivityViewModel
import com.example.playlistmaker.ui.search.vm.SearchActivityViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.ui.settings.SettingsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModuleModule = module {

    viewModel{
        FragsHolderActivityViewModel(get())
    }

    viewModel{
        MediaActivityViewModel(get())
    }

    viewModel{
        SearchActivityViewModel(get())
    }

    viewModel{
        SettingsFragmentViewModel(get())
    }

    viewModel{
        FavSongsFragmentViewModel()
    }

    viewModel{
        PlaylistsFragmentViewModel()
    }
}