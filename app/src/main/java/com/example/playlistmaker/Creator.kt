package com.example.playlistmaker


import android.content.Context
import com.example.playlistmaker.data.level_3_repositories.AppPrefsRepositoryImpl
import com.example.playlistmaker.domain.level_1_entities.AppPrefsRepository
import com.example.playlistmaker.domain.level_1_entities.Interactor
import com.example.playlistmaker.domain.level_2_use_cases.InteractorImpl


object Creator {

    private fun getAppPrefsRepository( context: Context ) :AppPrefsRepository {
        val x = AppPrefsRepositoryImpl()
        x.init( context )
        return x
    }

    fun provideInteractor( context :Context ) :Interactor {
        return  InteractorImpl( getAppPrefsRepository( context ) )
    }

}