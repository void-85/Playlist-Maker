package com.example.playlistmaker.ui.player.act


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.ui.player.vm.MediaActivityScreenUpdate
import com.example.playlistmaker.ui.player.vm.MediaActivityViewModel
import com.example.playlistmaker.ui.utils.millisToMinSec


class MediaActivity : AppCompatActivity() {

    private lateinit var goBackButton: ImageView
    private lateinit var mediaArtwork: ImageView

    private lateinit var mediaTimeCode: TextView
    private lateinit var mediaTitle: TextView
    private lateinit var mediaArtist: TextView
    private lateinit var mediaLength: TextView
    private lateinit var mediaAlbum: TextView
    private lateinit var mediaAlbumHdr: TextView
    private lateinit var mediaDate: TextView
    private lateinit var mediaGenre: TextView
    private lateinit var mediaCountry: TextView

    private lateinit var playPauseButton: ImageSwitcher

    private lateinit var binding: ActivityMediaBinding
    private lateinit var viewModel: MediaActivityViewModel


    private var intentionalExit: Boolean = false
    private var showPlayButtonElsePauseButton: Boolean = true
    private fun updatePlayPauseButtonStateFromVar() {

        when (AppCompatDelegate.getDefaultNightMode()) {

            AppCompatDelegate.MODE_NIGHT_YES -> {
                if (showPlayButtonElsePauseButton) {
                    playPauseButton.setImageResource(R.drawable.icon_play_dark)
                } else {
                    playPauseButton.setImageResource(R.drawable.icon_pause_dark)
                }
            }

            else -> {
                if (showPlayButtonElsePauseButton) {
                    playPauseButton.setImageResource(R.drawable.icon_play)
                } else {
                    playPauseButton.setImageResource(R.drawable.icon_pause)
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()

        viewModel.onStartActivity()
    }

    override fun onStop() {
        super.onStop()

        viewModel.onStopActivity()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        intentionalExit = true
        //viewModel.pause()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (intentionalExit) {
            viewModel.setMediaPlayerLastPositionToStart()
        } else {
            viewModel.setMediaPlayerLastPosition()
        }

        viewModel.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(null) //savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(
            this,
            MediaActivityViewModel.getViewModelFactory()
        )[MediaActivityViewModel::class.java]
        viewModel.getState().observe(this) {

            when (it) {
                is MediaActivityScreenUpdate.AllData -> {

                    Glide
                        .with(this)
                        .load(it.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                        .placeholder(R.drawable.spiral)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(
                                resources.getDimensionPixelSize(
                                    R.dimen.media_screen_artwork_corner_radius
                                )
                            )
                        )
                        .into(mediaArtwork)

                    mediaTitle.text = it.mediaTitle
                    mediaArtist.text = it.mediaArtist
                    mediaLength.text = it.mediaLength

                    if (it.mediaAlbum.isNotEmpty()) {
                        mediaAlbum.text = it.mediaAlbum
                    } else {
                        mediaAlbum.visibility = View.GONE
                        mediaAlbumHdr.visibility = View.GONE
                    }

                    mediaDate.text = it.mediaDate.substringBefore("-")
                    mediaGenre.text = it.mediaGenre
                    mediaCountry.text = it.mediaCountry

                    showPlayButtonElsePauseButton = it.showPlayElsePauseButtonState
                }

                is MediaActivityScreenUpdate.TimeCodeOnly -> {
                    mediaTimeCode.text = it.timeCode.millisToMinSec()
                }

                is MediaActivityScreenUpdate.ShowPlayElsePauseButtonStateOnly -> {
                    showPlayButtonElsePauseButton = it.state
                    updatePlayPauseButtonStateFromVar()
                }

                is MediaActivityScreenUpdate.PlayFinished -> {
                    mediaTimeCode.text = 0L.millisToMinSec()
                    showPlayButtonElsePauseButton = true
                    updatePlayPauseButtonStateFromVar()
                }

                else -> {}
            }
        }


        playPauseButton = binding.mediaScreenPlay
        playPauseButton.setFactory {

            val myView = ImageView(applicationContext)
            myView.scaleType = ImageView.ScaleType.FIT_CENTER
            myView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            myView
        }
        playPauseButton.inAnimation = AnimationUtils.loadAnimation(this, R.anim.switches_in)
        playPauseButton.outAnimation = AnimationUtils.loadAnimation(this, R.anim.switches_out)

        updatePlayPauseButtonStateFromVar()

        playPauseButton.setOnClickListener {

            viewModel.playPauseButtonPressed()
        }

        mediaTimeCode = binding.mediaScreenTimeCode

        mediaArtwork = binding.mediaScreenArtwork
        mediaTitle = binding.mediaScreenSongTitle
        mediaArtist = binding.mediaScreenSongArtist

        mediaLength = binding.mediaScreenDetails1LineData
        mediaAlbum = binding.mediaScreenDetails2LineData
        mediaAlbumHdr = binding.mediaScreenDetails2Line
        mediaDate = binding.mediaScreenDetails3LineData
        mediaGenre = binding.mediaScreenDetails4LineData
        mediaCountry = binding.mediaScreenDetails5LineData

        goBackButton = binding.mediaScreenBackButton
        goBackButton.setOnClickListener {
            intentionalExit = true
            viewModel.pause()
            finish()
        }
    }
}