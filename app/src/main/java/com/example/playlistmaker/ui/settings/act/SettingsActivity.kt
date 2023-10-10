package com.example.playlistmaker.ui.settings.act


import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.vm.SettingsActivityViewModel


class SettingsActivity (): AppCompatActivity() {

    private lateinit var settingsThemeSwitcher: SwitchMaterial
    private lateinit var binding: ActivitySettingsBinding

    //private lateinit var viewModel: SettingsActivityViewModel
    private val viewModel by viewModel<SettingsActivityViewModel>()

    private val setTheme: ((Boolean) -> Unit) = { darkThemeEnabled ->
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
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setThemeSwitchFun( setTheme )
        viewModel.getSwitchToDarkThemeState().observe(this) {
            settingsThemeSwitcher.isChecked = it
        }


        settingsThemeSwitcher = binding.settingsThemeSwitcher
        settingsThemeSwitcher.setOnCheckedChangeListener { _, checked ->
            run { viewModel.setSwitchToDarkThemeState(checked) }
        }


        val goBackButtonId = binding.settingsGoBackButton
        goBackButtonId.setOnClickListener { finish() }


        val shareAppButtonId = binding.settingsShareAppButton
        shareAppButtonId.setOnClickListener {

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_app_link))
                flags = FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }


        val supportButtonId = binding.settingsSupportButton
        supportButtonId.setOnClickListener {

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_support_text))
            }
            startActivity( Intent.createChooser(sendIntent, null) )
        }


        val userAgreementButtonId = binding.settingsUserAgreementButton
        userAgreementButtonId.setOnClickListener {

            val sendIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.settings_user_agreement_link))
            }
            startActivity(Intent.createChooser(sendIntent, null))
        }
    }

}