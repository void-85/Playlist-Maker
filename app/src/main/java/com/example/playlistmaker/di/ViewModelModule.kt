package com.example.playlistmaker.di


import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.FavSongsFragmentViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.FragsHolderActivityViewModel
import com.example.playlistmaker.ui.playerActivity.vm.MediaActivityViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.ui.search.SearchFragmentViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.ui.settings.SettingsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModuleModule = module {

    viewModel {
        FragsHolderActivityViewModel(get())
    }

    viewModel {
        MediaActivityViewModel(get())
    }

    viewModel {
        SearchFragmentViewModel(get())
    }

    viewModel {
        SettingsFragmentViewModel(get())
    }

    viewModel {
        FavSongsFragmentViewModel(get())
    }

    viewModel {
        PlaylistsFragmentViewModel()
    }
}