package com.example.playlistmaker.ui.fragsHolderActivity.ui.settings


import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding


class SettingsFragment: Fragment() {

    private lateinit var settingsThemeSwitcher: SwitchMaterial
    private lateinit var binding: FragmentSettingsBinding

    //private lateinit var viewModel: SettingsActivityViewModel
    private val viewModel by viewModel<SettingsFragmentViewModel>()

    private val setTheme: ((Boolean) -> Unit) = { darkThemeEnabled ->
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view:View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        /*binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)*/

        viewModel.setThemeSwitchFun( setTheme )
        viewModel.getSwitchToDarkThemeState().observe(viewLifecycleOwner) {
            settingsThemeSwitcher.isChecked = it
        }

        settingsThemeSwitcher = binding.settingsThemeSwitcher
        settingsThemeSwitcher.setOnCheckedChangeListener { _, checked ->
            run { viewModel.setSwitchToDarkThemeState(checked) }
        }

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