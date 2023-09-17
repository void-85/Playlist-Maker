package com.example.playlistmaker.presentation.level_4_ui

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.level_1_entities.Track
import com.example.playlistmaker.interactor
import com.example.playlistmaker.presentation.level_3_presenters.millisToMinSec

class MediaActivity : AppCompatActivity() {

    private lateinit var goBackButton :ImageView
    private lateinit var mediaArtwork :ImageView

    private lateinit var mediaTimeCode:TextView
    private lateinit var mediaTitle   :TextView
    private lateinit var mediaArtist  :TextView
    private lateinit var mediaLength  :TextView
    private lateinit var mediaAlbum   :TextView
    private lateinit var mediaAlbumHdr:TextView
    private lateinit var mediaDate    :TextView
    private lateinit var mediaGenre   :TextView
    private lateinit var mediaCountry :TextView

    private lateinit var playPauseButton :ImageSwitcher

    private var showPlayButtonElsePauseButton: Boolean = true
    private var intentionalExit: Boolean = false



    private fun updatePlayPauseButtonStateFromVar(){

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

    private fun onPlayFun() {
        showPlayButtonElsePauseButton = false
        updatePlayPauseButtonStateFromVar()
    }
    private fun onPauseFun() {
        showPlayButtonElsePauseButton = true
        updatePlayPauseButtonStateFromVar()
    }
    private fun onCompletionFun() {
        showPlayButtonElsePauseButton = true
        updatePlayPauseButtonStateFromVar()

        mediaTimeCode.text = getString(R.string.media_initial_time)
    }
    private fun updateFun() {
        mediaTimeCode.text = interactor.getCurrentPosition()
            .millisToMinSec()
    }



    override fun onStart() {
        super.onStart()

        if (interactor.isMediaPlayerToResumeOnCreate()) { interactor.start() }

    }

    override fun onStop() {
        super.onStop()

        interactor.setMediaPlayerToResumeOnCreate( interactor.isPlaying() )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null) //savedInstanceState)
        setContentView(R.layout.activity_media)

        playPauseButton = findViewById(R.id.media_screen_play)
        playPauseButton.setFactory {

            val myView = ImageView(applicationContext)
            myView.scaleType = ImageView.ScaleType.FIT_CENTER
            myView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            myView
        }

        playPauseButton.inAnimation  = AnimationUtils.loadAnimation(this, R.anim.switches_in)
        playPauseButton.outAnimation = AnimationUtils.loadAnimation(this, R.anim.switches_out)

        // activity init drawable
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> { playPauseButton.setImageResource(R.drawable.icon_play_dark) }
            else ->                             { playPauseButton.setImageResource(R.drawable.icon_play)      }
        }

        playPauseButton.setOnClickListener {

            if( interactor.isPlaying() ) interactor.pause()
            else                         interactor.start()
        }



        mediaTimeCode= findViewById(R.id.media_screen_time_code)

        mediaArtwork = findViewById(R.id.media_screen_artwork)
        mediaTitle   = findViewById(R.id.media_screen_song_title)
        mediaArtist  = findViewById(R.id.media_screen_song_artist)

        mediaLength  = findViewById(R.id.media_screen_details_1_line_data)
        mediaAlbum   = findViewById(R.id.media_screen_details_2_line_data)
        mediaAlbumHdr= findViewById(R.id.media_screen_details_2_line)
        mediaDate    = findViewById(R.id.media_screen_details_3_line_data)
        mediaGenre   = findViewById(R.id.media_screen_details_4_line_data)
        mediaCountry = findViewById(R.id.media_screen_details_5_line_data)

        goBackButton = findViewById(R.id.media_screen_back_button)
        goBackButton.setOnClickListener {
            intentionalExit = true
            interactor.pause()
            finish()
        }


        val json :String = interactor.getCurrentlyPlaying()
        if (json.isNotEmpty()) {

            val data = Gson().fromJson<Track>(
                    json,
                    object : TypeToken<Track>() {}.type
                )

            Glide
                .with( this)
                .load( data.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg") )
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

            mediaTitle.text  = data.trackName
            mediaArtist.text = data.artistName
            mediaLength.text = data.trackTime

            if(data.collectionName.isNotEmpty()){
                mediaAlbum.text = data.collectionName
            }else{
                mediaAlbum.visibility    = View.GONE
                mediaAlbumHdr.visibility = View.GONE
            }

            mediaDate.text    = data.releaseDate.substringBefore("-")
            mediaGenre.text   = data.primaryGenreName
            mediaCountry.text = data.country

            interactor.prepare (
                data.previewUrl   ,
                interactor.getMediaPlayerLastPosition().toInt() ,
                interactor.isMediaPlayerToResumeOnCreate()      ,
                ::updateFun       ,
                ::onCompletionFun ,
                ::onPlayFun       ,
                ::onPauseFun      )
        }

    }


    override fun onDestroy() {

        super.onDestroy()
        interactor.pause()

        if(intentionalExit){

            interactor.setMediaPlayerLastPosition(0L)
            interactor.setMediaPlayerToResumeOnCreate(false)

        }else{

            interactor.setMediaPlayerLastPosition( interactor.getCurrentPosition() /*mediaPlayer.currentPosition.toLong()*/ )

        }

        interactor.release()

    }
}