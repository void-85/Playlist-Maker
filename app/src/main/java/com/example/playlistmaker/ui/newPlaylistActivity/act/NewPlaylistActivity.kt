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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityNewPlaylistBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.newPlaylistActivity.vm.NewPlaylistActivityViewModel
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset


class NewPlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPlaylistBinding
    private val viewModel by viewModel<NewPlaylistActivityViewModel>()

    private lateinit var toolbar: Toolbar
    private lateinit var playlistImage: ImageView
    private lateinit var playlistName: TextInputEditText
    private lateinit var playlistDescription: TextInputEditText
    private lateinit var createPlaylistButton: Button

    private var currentImageURI: Uri? = null


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
                Glide
                    .with(applicationContext)
                    .load(uri)
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
        playlistImage = binding.newPlaylistUploadImage
        playlistImage.setOnClickListener {
            uploadPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        toolbar = binding.newPlaylistToolbar
        toolbar.setNavigationOnClickListener {
            Toast.makeText(applicationContext, "exit...", Toast.LENGTH_LONG).show()
        }


        playlistName = binding.playlistName
        playlistName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { /**/ }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {/**/}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createPlaylistButton.isEnabled = (s?.length ?: 0) > 0
            }
        })


        playlistDescription = binding.playlistDescription


        createPlaylistButton = binding.createNewPlaylistButton
        createPlaylistButton.setOnClickListener {
            //Toast.makeText(applicationContext, "saving...", Toast.LENGTH_LONG).show()

            var imageIdFilename = ""
            if (currentImageURI != null) {

                imageIdFilename = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()

                val filePath = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")
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

            viewModel.createPlaylist( Playlist(
                id = 0L,

                name = playlistName.text.toString(),
                description = playlistDescription.text.toString(),

                imageId = imageIdFilename,

                emptyList(),
                0
            ))

        }
    }

}