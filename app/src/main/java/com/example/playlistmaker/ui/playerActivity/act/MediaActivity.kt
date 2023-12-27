package com.example.playlistmaker.ui.playerActivity.act


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.newPlaylistActivity.act.NewPlaylistActivity
import com.example.playlistmaker.ui.playerActivity.viewHolderAdapter.BottomSheetRecyclerViewPlaylistAdapter
import com.example.playlistmaker.ui.playerActivity.vm.MediaActivityScreenUpdate
import com.example.playlistmaker.ui.playerActivity.vm.MediaActivityViewModel
import com.example.playlistmaker.ui.utils.millisToMinSec
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MediaActivity : AppCompatActivity() {

    private val viewModel by viewModel<MediaActivityViewModel>()
    private lateinit var binding: ActivityMediaBinding

    private lateinit var bottomSheetRView: RecyclerView
    private val playlists = ArrayList<Playlist>()

    private lateinit var bottomSheetCreatePlaylistButton: Button

    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var overlay: View
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var goBackButton: ImageView
    private lateinit var mediaArtwork: ImageView
    private lateinit var addToPlaylistButton: ImageView

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
    private lateinit var favoriteTrackButton: ImageSwitcher


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

    private var trackIsFavorite: Boolean = false
    private fun updateTrackIsFavoriteButtonStateFromVar() {
        when (AppCompatDelegate.getDefaultNightMode()) {

            AppCompatDelegate.MODE_NIGHT_YES -> {
                if (trackIsFavorite) {
                    favoriteTrackButton.setImageResource(R.drawable.icon_liked_dark)
                } else {
                    favoriteTrackButton.setImageResource(R.drawable.icon_like_dark)
                }
            }

            else -> {
                if (trackIsFavorite) {
                    favoriteTrackButton.setImageResource(R.drawable.icon_liked)
                } else {
                    favoriteTrackButton.setImageResource(R.drawable.icon_like)
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

    private fun onIntentionalExit() {
        intentionalExit = true
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (intentionalExit) {
            viewModel.setMediaPlayerLastPositionToStart()
            viewModel.release()
        } else {
            viewModel.setMediaPlayerLastPosition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheetContainer = binding.playlistsBottomSheet
        overlay = binding.overlay
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = 0.5f + slideOffset
            }
        })

        bottomSheetRView = binding.bottomSheetRView
        bottomSheetRView.adapter = BottomSheetRecyclerViewPlaylistAdapter(playlists)

        bottomSheetCreatePlaylistButton = binding.mediaPlayerBottomSheetCreatePlaylist
        bottomSheetCreatePlaylistButton.setOnClickListener {

            if (!showPlayButtonElsePauseButton) {
                viewModel.playPauseButtonPressed()
            }

            val newPlaylistIntent = Intent(applicationContext, NewPlaylistActivity::class.java)
            startActivity(newPlaylistIntent)
        }


        onBackPressedDispatcher.addCallback(this) { onIntentionalExit() }

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

                    mediaTimeCode.text = it.timeCode.millisToMinSec()

                    showPlayButtonElsePauseButton = it.showPlayElsePauseButtonState
                    updatePlayPauseButtonStateFromVar()

                    trackIsFavorite = it.trackIsFavorite
                    updateTrackIsFavoriteButtonStateFromVar()

                    playlists.clear()
                    playlists.addAll(it.playlists)
                    bottomSheetRView.adapter?.notifyDataSetChanged()

                    viewModel.dataWasRecieved()
                }

                is MediaActivityScreenUpdate.TimeCodeOnly -> {
                    mediaTimeCode.text = it.timeCode.millisToMinSec()

                    viewModel.dataWasRecieved()
                }

                is MediaActivityScreenUpdate.ShowPlayElsePauseButtonStateOnly -> {
                    showPlayButtonElsePauseButton = it.state
                    updatePlayPauseButtonStateFromVar()

                    viewModel.dataWasRecieved()
                }

                is MediaActivityScreenUpdate.ShowTrackIsFavorite -> {
                    trackIsFavorite = it.trackIsFavorite
                    updateTrackIsFavoriteButtonStateFromVar()

                    viewModel.dataWasRecieved()
                }

                is MediaActivityScreenUpdate.PlayFinished -> {
                    mediaTimeCode.text = 0L.millisToMinSec()
                    showPlayButtonElsePauseButton = true
                    updatePlayPauseButtonStateFromVar()

                    viewModel.dataWasRecieved()
                }

                is MediaActivityScreenUpdate.BottomSheetPlaylistsOnly -> {
                    playlists.clear()
                    playlists.addAll(it.playlists)
                    bottomSheetRView.adapter?.notifyDataSetChanged()

                    viewModel.dataWasRecieved()
                }

                else -> {}
            }
        }

        addToPlaylistButton = binding.mediaScreenAddToPlaylist
        addToPlaylistButton.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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


        favoriteTrackButton = binding.mediaScreenLike
        favoriteTrackButton.setFactory {

            val myView = ImageView(applicationContext)
            myView.scaleType = ImageView.ScaleType.FIT_CENTER
            myView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            myView
        }
        favoriteTrackButton.inAnimation = AnimationUtils.loadAnimation(this, R.anim.likes_in)
        favoriteTrackButton.outAnimation = AnimationUtils.loadAnimation(this, R.anim.likes_out)
        favoriteTrackButton.setOnClickListener {
            viewModel.favoriteTrackButtonPressed(makeTrackFavorite = !trackIsFavorite)
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
        goBackButton.setOnClickListener { onIntentionalExit() }
    }
}