package com.example.playlistmaker.ui.fragsHolderActivity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityFragsHolderBinding

class FragsHolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragsHolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFragsHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = (
                supportFragmentManager
                    .findFragmentById(
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