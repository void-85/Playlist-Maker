package com.example.playlistmaker.ui.newPlaylistActivity.act

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityNewPlaylistBinding
import com.example.playlistmaker.di.Constants
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments.PlaylistsFragment
import com.example.playlistmaker.ui.newPlaylistActivity.vm.NewPlaylistActivityScreenUpdate
import com.example.playlistmaker.ui.newPlaylistActivity.vm.NewPlaylistActivityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class NewPlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPlaylistBinding
    private val viewModel by viewModel<NewPlaylistActivityViewModel>()

    private lateinit var toolbar: Toolbar
    private lateinit var playlistImage: ImageView
    private lateinit var playlistName: TextInputEditText
    private lateinit var playlistDescription: TextInputEditText
    private lateinit var createOrEditPlaylistButton: Button
    private lateinit var confirmExitDialog: MaterialAlertDialogBuilder

    private var playlist: Playlist? = null

    private var currentImageURI: Uri? = null
    private var editPlaylistId: Long? = null

    private fun renderImage(imageURI: Uri?) {
        if (imageURI is Uri) {
            Glide
                .with(applicationContext)
                .load(imageURI)
                .placeholder(R.drawable.spiral)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        resources.getDimensionPixelSize(
                            R.dimen.new_playlist_upload_image_corner_radius
                        )
                    )
                )
                .into(playlistImage)
        }
    }

    private fun checkIfEditMode() {

        val playlistJson = intent.extras?.getString(PlaylistsFragment.PLAYLIST_EDIT_MODE_KEY, null)
        if (playlistJson is String) {

            playlist =
                Gson().fromJson(playlistJson, object : TypeToken<Playlist>() {}.type)

            editPlaylistId = playlist?.id ?: 0

            toolbar.title = getString(R.string.new_edit_playlist_activity_title)
            playlistName.setText(playlist?.name ?: "")
            playlistDescription.setText(playlist?.description ?: "")
            createOrEditPlaylistButton.text =
                getString(R.string.new_edit_playlist_activity_create_button_caption)

            if ((playlist?.imageId ?: "") != "") {

                val filePath = File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    Constants.PLAYLISTS_IMAGE_FOLDER
                )
                val file = File(filePath, playlist?.imageId ?: "")
                currentImageURI = file.toUri()

                renderImage(currentImageURI)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        currentImageURI?.let {
            outState.putString(SELECTED_IMAGE_URI, it.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val uploadPhoto = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                //Toast.makeText(applicationContext, uri.toString(), Toast.LENGTH_LONG).show()
                //playlistImage.setImageURI(uri)
                currentImageURI = uri
                renderImage(currentImageURI)
            }
        }
        playlistImage = binding.newPlaylistUploadImage
        playlistImage.setOnClickListener {
            uploadPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        toolbar = binding.newPlaylistToolbar
        toolbar.setNavigationOnClickListener {
            //Toast.makeText(applicationContext, "exit...", Toast.LENGTH_LONG).show()
            onBackPressedDispatcher.onBackPressed()
        }


        playlistName = binding.playlistName
        playlistName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { /**/
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {/**/
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createOrEditPlaylistButton.isEnabled = (s?.length ?: 0) > 0
            }
        })


        playlistDescription = binding.playlistDescription


        createOrEditPlaylistButton = binding.createNewPlaylistButton
        createOrEditPlaylistButton.setOnClickListener {

            var imageIdFilename = ""
            if (currentImageURI != null) {

                imageIdFilename = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()

                val filePath =
                    File(
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        Constants.PLAYLISTS_IMAGE_FOLDER
                    )
                if (!filePath.exists()) {
                    filePath.mkdirs()
                }
                val file = File(filePath, imageIdFilename)
                val inputStream = contentResolver.openInputStream(currentImageURI!!)
                val outputStream = FileOutputStream(file)
                BitmapFactory
                    .decodeStream(inputStream)
                    .compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            playlist = Playlist(
                id = editPlaylistId ?: 0,

                name = playlistName.text.toString(),
                description = playlistDescription.text.toString(),

                imageId = imageIdFilename,

                tracks = playlist?.tracks ?: emptyList(),
                amountOfTracks = (playlist?.tracks ?: emptyList()).size
            )
            viewModel.createOrEditPlaylist(playlist!!)

        }

        confirmExitDialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.new_playlist_activity_exit_dialog_title))
            .setMessage(getString(R.string.new_playlist_activity_exit_dialog_text))
            .setNeutralButton(getString(R.string.new_playlist_activity_exit_dialog_cancel)) { _, _ -> /* NOTHING */ }
            .setPositiveButton(getString(R.string.new_playlist_activity_exit_dialog_terminate)) { _, _ -> finish() }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (
                    (createOrEditPlaylistButton.isEnabled || currentImageURI != null) &&
                    playlist !is Playlist
                ) {
                    confirmExitDialog.show()
                } else {
                    finish()
                }
            }
        })

        savedInstanceState?.let {
            val selectedImageUri = it.getString(SELECTED_IMAGE_URI, "")
            if (selectedImageUri.isNotEmpty()) {
                currentImageURI = selectedImageUri.toUri()
                renderImage(currentImageURI)
            }
        }

        checkIfEditMode()

        viewModel.getState().observe(this) {
            when (it) {
                is NewPlaylistActivityScreenUpdate.NotifyUserPlaylistCreated -> {
                    Toast.makeText(
                        applicationContext,

                        getString(R.string.new_playlist_activity_created_pre_msg) +
                                " \"${playlistName.text.toString()}\" " +
                                getString(R.string.new_playlist_activity_created_post_msg),

                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }

                is NewPlaylistActivityScreenUpdate.NotifyUserPlaylistEdited -> {
                    finish()
                }
            }

        }
    }

    companion object {
        const val SELECTED_IMAGE_URI = "SELECTED_IMAGE_URI"
        const val TIME_DELAY_FOR_DB_TRANSACTION = 300L
    }
}