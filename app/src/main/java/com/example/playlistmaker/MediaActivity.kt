package com.example.playlistmaker

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


class MediaActivity : AppCompatActivity() {

    private lateinit var goBackButton :ImageView
    private lateinit var mediaArtwork :ImageView

    private lateinit var mediaTitle   :TextView
    private lateinit var mediaArtist  :TextView
    private lateinit var mediaLength  :TextView
    private lateinit var mediaAlbum   :TextView
    private lateinit var mediaAlbumHdr:TextView
    private lateinit var mediaDate    :TextView
    private lateinit var mediaGenre   :TextView
    private lateinit var mediaCountry :TextView

    private lateinit var playPauseButton :ImageSwitcher



    private var playOrPause :Boolean = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            playOrPause = !playOrPause

            when( AppCompatDelegate.getDefaultNightMode() )
            {
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    if (playOrPause) { playPauseButton.setImageResource(R.drawable.icon_play_dark  ) }
                    else             { playPauseButton.setImageResource(R.drawable.icon_pause_dark ) }
                }

                else -> {
                    if (playOrPause) { playPauseButton.setImageResource(R.drawable.icon_play  ) }
                    else             { playPauseButton.setImageResource(R.drawable.icon_pause ) }
                }
            }

            /*when (applicationContext.resources
                    ?.configuration
                    ?.uiMode
                    ?.and(Configuration.UI_MODE_NIGHT_MASK))
            {
                Configuration.UI_MODE_NIGHT_YES -> {
                    if (playOrPause) { playPauseButton.setImageResource(R.drawable.icon_play_dark  ) }
                    else             { playPauseButton.setImageResource(R.drawable.icon_pause_dark ) }
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    if (playOrPause) { playPauseButton.setImageResource(R.drawable.icon_play  ) }
                    else             { playPauseButton.setImageResource(R.drawable.icon_pause ) }
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    if (playOrPause) { playPauseButton.setImageResource(R.drawable.icon_play  ) }
                    else             { playPauseButton.setImageResource(R.drawable.icon_pause ) }
                }
            }*/
        }



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
        goBackButton.setOnClickListener { finish() }

        val sharedPrefs = getSharedPreferences(App.PLAYLIST_PREFERENCES, MODE_PRIVATE)

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

            mediaTitle.text   = data.trackName
            mediaArtist.text  = data.artistName
            mediaLength.text  = data.trackTime

            if(data.collectionName.isNotEmpty()){
                mediaAlbum.text = data.collectionName
            }else{
                mediaAlbum.visibility    = View.GONE
                mediaAlbumHdr.visibility = View.GONE
            }

            mediaDate.text    = data.releaseDate.substringBefore("-")
            mediaGenre.text   = data.primaryGenreName
            mediaCountry.text = data.country

        }

    }
}