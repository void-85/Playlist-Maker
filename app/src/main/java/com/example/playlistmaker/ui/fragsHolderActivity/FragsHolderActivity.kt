package com.example.playlistmaker.ui.fragsHolderActivity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityFragsHolderBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragsHolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragsHolderBinding
    private val viewModel by viewModel<FragsHolderActivityViewModel>()

    val setTheme: ((Boolean) -> Unit) = { darkThemeEnabled ->
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFragsHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setThemeSwitchFun(setTheme)
        viewModel.applyCurrentTheme()

        val navView: BottomNavigationView = binding.navView
        navView.selectedItemId = R.id.libraryFragment
        val navController = (
                supportFragmentManager.findFragmentById(
                    R.id.nav_host_fragment_activity_frags_holder
                ) as NavHostFragment
                ).navController
        navView.setupWithNavController(navController)

        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/
    }
}






