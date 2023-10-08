package com.example.playlistmaker


import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModuleModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

//import com.example.playlistmaker.creator.Creator


class App : Application() {

    companion object {
        const val SEARCH_HISTORY_MAX_LENGTH = 10
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
        const val CLICK_DEBOUNCE_DELAY = 2_000L
        const val SEARCH_DEBOUNCE_REQ_MIN_LEN = 3
    }

    override fun onCreate() {
        super.onCreate()

        //Creator.init(this) // (App context) -> App ==> Creator ==> AppPrefsRepository ==> SharedPrefs
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModuleModule)
        }
    }
}