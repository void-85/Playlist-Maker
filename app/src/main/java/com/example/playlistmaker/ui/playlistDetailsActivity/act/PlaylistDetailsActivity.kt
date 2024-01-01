package com.example.playlistmaker.ui.playlistDetailsActivity.act


import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlaylistDetailsBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments.PlaylistsFragment
import com.example.playlistmaker.ui.playlistDetailsActivity.vm.PlaylistDetailsActivityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class PlaylistDetailsActivity : AppCompatActivity(){

    private val viewModel by viewModel<PlaylistDetailsActivityViewModel>()
    private lateinit var binding: ActivityPlaylistDetailsBinding

    private lateinit var playlistOnEdit : Playlist

    private lateinit var toolbar: Toolbar
    private lateinit var image: ImageView


    override fun onStart() {
        super.onStart()

        val filePath = File( getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")
        val file = File(filePath, playlistOnEdit.imageId)
        Glide
            .with(applicationContext)
            .load(file.toUri())
            .placeholder(R.drawable.spiral)
            .transform(CenterCrop())
            .into(image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlistJson = intent.extras?.getString(PlaylistsFragment.PLAYLIST_EDIT_MODE_KEY, null)
        if (playlistJson is String) {

            playlistOnEdit = Gson().fromJson(playlistJson, object : TypeToken<Playlist>() {}.type)

        }

        binding = ActivityPlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        image = binding.playlistImage

    }

}