package com.example.playlistmaker.ui.newPlaylistActivity.act

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.databinding.ActivityNewPlaylistBinding
import com.example.playlistmaker.ui.newPlaylistActivity.vm.NewPlaylistActivityViewModel
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNewPlaylistBinding
    private val viewModel by viewModel<NewPlaylistActivityViewModel>()

    private lateinit var toolbar: Toolbar
    private lateinit var playlistName: TextInputEditText
    private lateinit var playlistDescription: TextInputEditText
    private lateinit var createPlaylistButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.newPlaylistToolbar
        toolbar.setNavigationOnClickListener {
            Toast.makeText(applicationContext, "exit...", Toast.LENGTH_LONG).show()
        }

        createPlaylistButton = binding.createNewPlaylistButton

        playlistName = binding.playlistName
        playlistName.addTextChangedListener( object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                /**/
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /**/
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createPlaylistButton.isEnabled = (s?.length ?: 0) > 2
            }
        } )

    }

}