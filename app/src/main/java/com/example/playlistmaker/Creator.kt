package com.example.playlistmaker

import com.example.playlistmaker.data.level_3_repositories.AppPrefsRepositoryImpl
import com.example.playlistmaker.domain.level_1_entities.AppPrefsRepository
import com.example.playlistmaker.domain.level_1_entities.Interactor
import com.example.playlistmaker.domain.level_2_use_cases.InteractorImpl


object Creator {

    private fun getAppPrefsRepository() :AppPrefsRepository {
        return AppPrefsRepositoryImpl()
    }

    fun provideInteractor() :Interactor {
        return  InteractorImpl( getAppPrefsRepository() )
    }

}