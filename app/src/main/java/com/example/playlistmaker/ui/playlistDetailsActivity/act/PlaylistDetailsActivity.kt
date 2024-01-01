package com.example.playlistmaker.ui.playlistDetailsActivity.act


import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlaylistDetailsBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments.PlaylistsFragment
import com.example.playlistmaker.ui.playerActivity.act.MediaActivity
import com.example.playlistmaker.ui.playlistDetailsActivity.vhAdapter.BottomSheetRecyclerViewTrackAdapter
import com.example.playlistmaker.ui.playlistDetailsActivity.vm.PlaylistDetailsActivityScreenUpdate
import com.example.playlistmaker.ui.playlistDetailsActivity.vm.PlaylistDetailsActivityViewModel
import com.example.playlistmaker.ui.utils.toMinutesAmountString
import com.example.playlistmaker.ui.utils.toTrackAmountString
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class PlaylistDetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<PlaylistDetailsActivityViewModel>()
    private lateinit var binding: ActivityPlaylistDetailsBinding

    private lateinit var playlistOnEdit: Playlist

    private lateinit var toolbar: Toolbar
    private lateinit var image: ImageView
    private lateinit var name: TextView
    private lateinit var desc: TextView
    private lateinit var stats: TextView
    private lateinit var share: ImageView
    private lateinit var options: ImageView


    private lateinit var overlay: View
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetContainer: LinearLayout

    private lateinit var bottomSheetRView: RecyclerView
    private val tracks = ArrayList<Track>()
    private val trackClicked: (Track) -> Unit = {

        viewModel.playTrack(it)

        val mediaIntent = Intent(applicationContext, MediaActivity::class.java)
        startActivity(mediaIntent)
    }



    override fun onStart() {
        super.onStart()

        viewModel.refreshPlaylistData( playlistOnEdit.id )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlistJson = intent.extras?.getString(PlaylistsFragment.PLAYLIST_EDIT_MODE_KEY, null)
        if (playlistJson is String) {

            playlistOnEdit = Gson().fromJson(playlistJson, object : TypeToken<Playlist>() {}.type)
        }

        binding = ActivityPlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bottomSheetContainer = binding.bottomSheet
        overlay = binding.overlay
        overlay.alpha = 0.0f
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) { /**/ }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })

        bottomSheetRView = binding.bottomSheetRView
        bottomSheetRView.adapter = BottomSheetRecyclerViewTrackAdapter(tracks, trackClicked)


        toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        image   = binding.playlistImage
        name    = binding.name
        desc    = binding.description
        stats   = binding.statistics
        share   = binding.share
        options = binding.options

        viewModel.getState().observe(this){ state ->
            when(state){
                is PlaylistDetailsActivityScreenUpdate.PlaylistDataRefreshed -> {

                    val filePath = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")
                    val file = File(filePath, state.playlist.imageId )

                    if(state.playlist.imageId.isNotEmpty()){
                        image.setPadding( 0 )
                    }else{
                        image.setPadding( resources.getDimensionPixelSize( R.dimen.playlist_details_placeholder_padding ) )
                    }

                    Glide
                        .with(applicationContext)
                        .load(file.toUri())
                        .placeholder(R.drawable.spiral)
                        .transform(CenterCrop())
                        .into(image)

                    tracks.clear()
                    tracks.addAll( state.playlist.tracks )
                    bottomSheetRView.adapter?.notifyDataSetChanged()

                    name.text = state.playlist.name

                    if(state.playlist.description.isNotEmpty()){
                        desc.visibility = View.VISIBLE
                        desc.text = state.playlist.description
                    }else{
                        desc.visibility = View.GONE
                    }

                    var totalMinutes = 0
                    state.playlist.tracks.forEach {
                        totalMinutes += it.trackTime.split(":").first().toInt()
                    }
                    stats.text =
                        "${
                            totalMinutes.toMinutesAmountString(applicationContext)
                        } • ${
                            state.playlist.tracks.size.toTrackAmountString(applicationContext)
                        }"

                }
            }
        }
    }

}