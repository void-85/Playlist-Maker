package com.example.playlistmaker.di


import com.example.playlistmaker.ui.library.vm.FavSongsFragmentViewModel
import com.example.playlistmaker.ui.library.vm.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.main.vm.MainActivityViewModel
import com.example.playlistmaker.ui.player.vm.MediaActivityViewModel
import com.example.playlistmaker.ui.search.vm.SearchActivityViewModel
import com.example.playlistmaker.ui.settings.vm.SettingsActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModuleModule = module {

    viewModel{
        MainActivityViewModel(get())
    }

    viewModel{
        MediaActivityViewModel(get())
    }

    viewModel{
        SearchActivityViewModel(get())
    }

    viewModel{
        SettingsActivityViewModel(get())
    }

    viewModel{
        FavSongsFragmentViewModel()
    }

    viewModel{
        PlaylistsFragmentViewModel()
    }
}