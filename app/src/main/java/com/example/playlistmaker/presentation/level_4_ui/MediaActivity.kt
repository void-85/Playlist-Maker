package com.example.playlistmaker.presentation.level_4_ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

import com.example.playlistmaker.App
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



    private var mediaPlayer = MediaPlayer()
    companion object {
        private const val STATE_DEFAULT  = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING  = 2
        private const val STATE_PAUSED   = 3
    }
    private var playerState = STATE_DEFAULT

    private var showPlayButtonElsePauseButton: Boolean = true
    private var intentionalExit: Boolean = false



    private val handler = Handler( Looper.getMainLooper() )
    private val updatePosRunnable =
        Runnable {
            mediaTimeCode.text = mediaPlayer.currentPosition.toLong().millisToMinSec()
            schedulePositionUpdate()
        }


    private fun schedulePositionUpdate(){
        handler.postDelayed(updatePosRunnable, App.MEDIA_PLAYER_UPDATE_POS_PERIOD)
    }
    private fun clearSchedule(){
        handler.removeCallbacks(updatePosRunnable)
    }



    override fun onBackPressed(){
        intentionalExit = true
        clearSchedule()
        finish()
    }




    private fun updatePlayPauseButtonStateFromVar(){

        when( AppCompatDelegate.getDefaultNightMode() )
        {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                if (showPlayButtonElsePauseButton) { playPauseButton.setImageResource(R.drawable.icon_play_dark) }
                else                               { playPauseButton.setImageResource(R.drawable.icon_pause_dark) }
            }

            else -> {
                if (showPlayButtonElsePauseButton) { playPauseButton.setImageResource(R.drawable.icon_play) }
                else                               { playPauseButton.setImageResource(R.drawable.icon_pause) }
            }
        }
    }

    private fun startPlayer() {

        mediaPlayer.start()

        showPlayButtonElsePauseButton = false
        updatePlayPauseButtonStateFromVar()

        playerState = STATE_PLAYING

        schedulePositionUpdate()
    }

    private fun pausePlayer() {

        clearSchedule()

        mediaPlayer.pause()

        showPlayButtonElsePauseButton = true
        updatePlayPauseButtonStateFromVar()

        playerState = STATE_PAUSED

    }

    private fun preparePlayer( url :String ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {

            playerState = STATE_PREPARED

            var playPosition = 0
            var resumePlay   = false

            playPosition = interactor.getMediaPlayerLastPosition().toInt()
            resumePlay   = interactor.isMediaPlayerToResumeOnCreate()

            mediaPlayer.seekTo( playPosition )
            mediaTimeCode.text = mediaPlayer.currentPosition.toLong().millisToMinSec()
            if( resumePlay ) startPlayer()

        }

        mediaPlayer.setOnCompletionListener {

            playerState = STATE_PREPARED

            clearSchedule()

            showPlayButtonElsePauseButton = true
            updatePlayPauseButtonStateFromVar()

            mediaTimeCode.text = getString(R.string.media_initial_time)
        }
    }


    override fun onStart() {
        super.onStart()

        if (interactor.isMediaPlayerToResumeOnCreate()) {
            startPlayer()
        }

    }

    override fun onStop() {
        super.onStop()

        val resumePlay = (playerState == STATE_PLAYING)
        pausePlayer()

        interactor.setMediaPlayerToResumeOnCreate( resumePlay )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null) //savedInstanceState)
        setContentView(R.layout.activity_media)

        //sharedPrefsMA = getSharedPreferences(App.PLAYLIST_PREFERENCES, MODE_PRIVATE)

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

            when(playerState) {
                STATE_PLAYING                -> { pausePlayer() }
                STATE_PREPARED, STATE_PAUSED -> { startPlayer() }
            }
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
            clearSchedule()
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

            preparePlayer( data.previewUrl )

        }

    }


    override fun onDestroy() {

        super.onDestroy()
        clearSchedule()

        if(intentionalExit){

            interactor.setMediaPlayerLastPosition(0L)
            interactor.setMediaPlayerToResumeOnCreate(false)

        }else{

            interactor.setMediaPlayerLastPosition( mediaPlayer.currentPosition.toLong() )

        }

        mediaPlayer.release()

    }
}