package com.example.playlistmaker.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.google.android.material.switchmaterial.SwitchMaterial


import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.interactor


class SettingsActivity : AppCompatActivity() {



    private lateinit var settingsThemeSwitcher :SwitchMaterial



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        // Exception ? to get SharedPrefs
        //val sharedPrefs = getSharedPreferences(App.PLAYLIST_PREFERENCES, MODE_PRIVATE)

        settingsThemeSwitcher = findViewById<SwitchMaterial>(R.id.settings_theme_switcher)
        settingsThemeSwitcher.isChecked = interactor.isThemeDark()
        settingsThemeSwitcher.setOnCheckedChangeListener {

            _, checked -> run{
                (applicationContext as App).setTheme(checked)
                interactor.setDarkTheme( checked )
            }
        }



        val goBackButtonId = findViewById<FrameLayout>(R.id.settings_go_back_button)
        goBackButtonId.setOnClickListener{

            finish()
        }


        val shareAppButtonId = findViewById<FrameLayout>(R.id.settings_share_app_button)
        shareAppButtonId.setOnClickListener{

            val sendIntent = Intent().apply{
                action = Intent.ACTION_SEND
                type   = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_app_link))
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }


        val supportButtonId = findViewById<FrameLayout>(R.id.settings_support_button)
        supportButtonId.setOnClickListener{

            val sendIntent = Intent().apply{
                action = Intent.ACTION_SENDTO
                data   = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf( getString(R.string.settings_support_email)))
                putExtra(Intent.EXTRA_SUBJECT,        getString(R.string.settings_support_subject))
                putExtra(Intent.EXTRA_TEXT,           getString(R.string.settings_support_text))
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }


        val userAgreementButtonId = findViewById<FrameLayout>(R.id.settings_user_agreement_button)
        userAgreementButtonId.setOnClickListener{

            val sendIntent = Intent().apply{
                action = Intent.ACTION_VIEW
                data   = Uri.parse( getString(R.string.settings_user_agreement_link))
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }
    }
}