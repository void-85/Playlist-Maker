package com.example.playlistmaker.ui.playlistDetailsActivity.act


import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import com.example.playlistmaker.ui.newPlaylistActivity.act.NewPlaylistActivity
import com.example.playlistmaker.ui.playerActivity.act.MediaActivity
import com.example.playlistmaker.ui.playlistDetailsActivity.vhAdapter.BottomSheetRecyclerViewTrackAdapter
import com.example.playlistmaker.ui.playlistDetailsActivity.vm.PlaylistDetailsActivityScreenUpdate
import com.example.playlistmaker.ui.playlistDetailsActivity.vm.PlaylistDetailsActivityViewModel
import com.example.playlistmaker.ui.utils.toMinutesAmountString
import com.example.playlistmaker.ui.utils.toTrackAmountString
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    private lateinit var bottomSheetBehavior2: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetContainer2: LinearLayout

    private lateinit var bottomSheetRView: RecyclerView
    private val tracks = ArrayList<Track>()
    private val trackClicked: (Track) -> Unit = {

        viewModel.playTrack(it)

        val mediaIntent = Intent(applicationContext, MediaActivity::class.java)
        startActivity(mediaIntent)
    }
    private val trackLongTouched: (Track) -> Unit = {

        viewModel.deleteTrackFromPlaylist(it, playlistOnEdit)
    }


    override fun onStart() {
        super.onStart()

        viewModel.refreshPlaylistData(playlistOnEdit.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlistJson = intent.extras?.getString(PlaylistsFragment.PLAYLIST_EDIT_MODE_KEY, null)
        if (playlistJson is String) {

            playlistOnEdit = Gson().fromJson(playlistJson, object : TypeToken<Playlist>() {}.type)
        }

        binding = ActivityPlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bottomSheetContainer2 = findViewById<LinearLayout>(R.id.bottom_sheet_edit_playlist)
        bottomSheetBehavior2 = BottomSheetBehavior.from(bottomSheetContainer2).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior2.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.alpha = 0.0f
                        if(tracks.isEmpty()){
                            bottomSheetContainer.visibility = View.INVISIBLE
                        }else{
                            bottomSheetContainer.visibility = View.VISIBLE
                        }
                    }

                    else -> {
                        overlay.alpha = 1.0f
                        bottomSheetContainer.visibility = View.INVISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { /**/
            }
        })


        bottomSheetContainer = binding.bottomSheet
        overlay = binding.overlay
        overlay.alpha = 0.0f
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) { /**/
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })

        bottomSheetRView = binding.bottomSheetRView
        bottomSheetRView.adapter =
            BottomSheetRecyclerViewTrackAdapter(tracks, trackClicked, trackLongTouched)



        toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        image = binding.playlistImage
        name = binding.name
        desc = binding.description
        stats = binding.statistics

        share = binding.share
        share.setOnClickListener {
            if (playlistOnEdit.tracks.isNotEmpty()) {

                var msg: String =
                    "\"${playlistOnEdit.name}\"\n${playlistOnEdit.description}\n${
                        playlistOnEdit.tracks.size.toTrackAmountString(applicationContext)
                    }"

                var number = 0
                playlistOnEdit.tracks.forEach {
                    number++
                    msg += "\n${number}. ${it.artistName} - ${it.trackName} (${it.trackTime})"
                }

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, msg)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(Intent.createChooser(sendIntent, null))

            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.edit_playlist_share_nothing_to_share),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        options = binding.options
        options.setOnClickListener {
            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        findViewById<TextView>(R.id.bottom_sheet_share).setOnClickListener {

            share.performClick()
        }

        findViewById<TextView>(R.id.bottom_sheet_edit).setOnClickListener {

            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_HIDDEN

            val mediaIntent = Intent(applicationContext, NewPlaylistActivity::class.java)
            mediaIntent.putExtra(PlaylistsFragment.PLAYLIST_EDIT_MODE_KEY, Gson().toJson(playlistOnEdit))
            startActivity(mediaIntent)
        }

        findViewById<TextView>(R.id.bottom_sheet_delete).setOnClickListener {

            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_HIDDEN

            MaterialAlertDialogBuilder(applicationContext)
                .setTitle(getString(R.string.edit_playlist_bottom_sheet_delete_title))
                .setMessage(getString(R.string.edit_playlist_bottom_sheet_delete_msg))
                .setNegativeButton(getString(R.string.edit_playlist_bottom_sheet_delete_cancel)){ _,_ -> /**/ }
                .setPositiveButton(getString(R.string.edit_playlist_bottom_sheet_delete_delete)){ _,_ ->
                    viewModel.deletePlaylist(playlistOnEdit.id)
                    finish()
                }.show()

        }



        viewModel.getState().observe(this) { state ->
            when (state) {
                is PlaylistDetailsActivityScreenUpdate.PlaylistDataRefreshed -> {

                    playlistOnEdit = state.playlist

                    val filePath =
                        File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")
                    val file = File(filePath, state.playlist.imageId)

                    if (state.playlist.imageId.isNotEmpty()) {
                        image.setPadding(0)
                    } else {
                        image.setPadding(resources.getDimensionPixelSize(R.dimen.playlist_details_placeholder_padding))
                    }

                    Glide
                        .with(applicationContext)
                        .load(file.toUri())
                        .placeholder(R.drawable.spiral)
                        .transform(CenterCrop())
                        .into(image)


                    // bottom sheet view holder ----------------------------------------------------
                    Glide
                        .with(applicationContext)
                        .load(file.toUri())
                        .placeholder(R.drawable.spiral)
                        .transform(CenterCrop())
                        .into( findViewById<ImageView>(R.id.edit_image) )

                    findViewById<TextView>(R.id.edit_name).text = state.playlist.name
                    findViewById<TextView>(R.id.edit_number_of_tracks).text = state.playlist.tracks.size.toTrackAmountString(applicationContext)
                    // bottom sheet view holder ----------------------------------------------------





                    tracks.clear()
                    tracks.addAll(state.playlist.tracks)
                    bottomSheetRView.adapter?.notifyDataSetChanged()

                    if(tracks.isEmpty()){
                        bottomSheetContainer.visibility = View.INVISIBLE
                    }else{
                        bottomSheetContainer.visibility = View.VISIBLE
                    }

                    name.text = state.playlist.name

                    if (state.playlist.description.isNotEmpty()) {
                        desc.visibility = View.VISIBLE
                        desc.text = state.playlist.description
                    } else {
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