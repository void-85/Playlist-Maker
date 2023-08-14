package com.example.playlistmaker

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

    private var resumePlayOnCreate   :Boolean = false
    private var playPauseButtonState :Boolean = true
    private var playPosition         :Long    = 0L



    private val handler = Handler( Looper.getMainLooper() )
    private val updatePosRunnable =
        Runnable {
            mediaTimeCode.text = mediaPlayer.currentPosition.toLong().millisToMinSec()
            schedulePosUpdate()
        }


    private fun schedulePosUpdate(){
        handler.postDelayed(updatePosRunnable, App.MEDIA_PLAYER_UPDATE_POS_PERIOD)
    }
    private fun clearSchedule(){
        handler.removeCallbacks(updatePosRunnable)
    }



    @Deprecated("Deprecated in Java")
    override fun onBackPressed(){
        exitActivity()
    }
    private fun exitActivity(){
        if( playerState == STATE_PLAYING ) { pausePlayer() }
        finish()
    }



    private fun updatePlayPauseButtonStateFromVar(){

        when( AppCompatDelegate.getDefaultNightMode() )
        {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                if (playPauseButtonState) { playPauseButton.setImageResource(R.drawable.icon_play_dark  ) }
                else                      { playPauseButton.setImageResource(R.drawable.icon_pause_dark ) }
            }

            else -> {
                if (playPauseButtonState) { playPauseButton.setImageResource(R.drawable.icon_play  ) }
                else                      { playPauseButton.setImageResource(R.drawable.icon_pause ) }
            }
        }
    }

    private fun startPlayer() {

        mediaPlayer.start()

        playPauseButtonState = false
        updatePlayPauseButtonStateFromVar()

        playerState = STATE_PLAYING

        schedulePosUpdate()
    }

    private fun pausePlayer() {

        clearSchedule()

        mediaPlayer.pause()

        playPauseButtonState = true
        updatePlayPauseButtonStateFromVar()

        playerState = STATE_PAUSED

    }

    private fun preparePlayer( url :String ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {

            playerState = STATE_PREPARED

            if( resumePlayOnCreate ) {

                mediaPlayer.seekTo( playPosition.toInt() )
                startPlayer()
            }
        }

        mediaPlayer.setOnCompletionListener {

            clearSchedule()

            sharedPrefs
                .edit()
                .putBoolean(App.MEDIA_PLAYER_RESUME_PLAY_ON_CREATE_KEY, false )
                .putLong   (App.MEDIA_PLAYER_LAST_POSITION_LONG_KEY,    0L    )
                .apply()

            playPauseButtonState = true
            updatePlayPauseButtonStateFromVar()

            playerState = STATE_PREPARED


            mediaTimeCode.text = getString( R.string.media_initial_time )
        }
    }



    override fun onStop() {
        super.onStop()
        if( playerState == STATE_PLAYING ) { pausePlayer() }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null) //savedInstanceState)
        setContentView(R.layout.activity_media)


        playPauseButton = findViewById( R.id.media_screen_play )
        playPauseButton.setFactory {

            val myView = ImageView(applicationContext)
            myView.scaleType = ImageView.ScaleType.FIT_CENTER
            myView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            myView
        }

        playPauseButton.inAnimation  = AnimationUtils.loadAnimation(this, R.anim.switches_in  )
        playPauseButton.outAnimation = AnimationUtils.loadAnimation(this, R.anim.switches_out )

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
        goBackButton.setOnClickListener { exitActivity() }


        val sharedPrefs = getSharedPreferences(App.PLAYLIST_PREFERENCES, MODE_PRIVATE)



        resumePlayOnCreate = sharedPrefs.getBoolean( App.MEDIA_PLAYER_RESUME_PLAY_ON_CREATE_KEY, false )
        playPosition       = sharedPrefs.getLong   ( App.MEDIA_PLAYER_LAST_POSITION_LONG_KEY,    0L    )

        val json = sharedPrefs.getString(App.CURRENTLY_PLAYING_KEY, "") ?: ""
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

        if( playerState == STATE_PLAYING ) {
            pausePlayer()
            playPosition = mediaPlayer.currentPosition.toLong()
            sharedPrefs
                .edit()
                .putBoolean(App.MEDIA_PLAYER_RESUME_PLAY_ON_CREATE_KEY, true         )
                .putLong   (App.MEDIA_PLAYER_LAST_POSITION_LONG_KEY,    playPosition )
                .apply()
        }else{
            sharedPrefs
                .edit()
                .putBoolean(App.MEDIA_PLAYER_RESUME_PLAY_ON_CREATE_KEY, false )
                .putLong   (App.MEDIA_PLAYER_LAST_POSITION_LONG_KEY,    0L    )
                .apply()
        }

        mediaPlayer.release()
    }
}