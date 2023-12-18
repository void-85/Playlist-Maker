package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.databinding.FragmentFavSongsBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.FavSongsFragmentScreenUpdate
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.FavSongsFragmentViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.viewHolderAdapter.RecyclerViewTrackAdapter
import com.example.playlistmaker.ui.player.act.MediaActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FavSongsFragment : Fragment() {

    companion object {

        fun newInstance() = FavSongsFragment().apply {
            /*arguments = Bundle().apply {
                putInt(TEXT_RESOURCE, textResource)
            }*/
        }

        const val CLICK_DEBOUNCE_DELAY_MILLIS = 2_000L
    }

    private val viewmodel by viewModel<FavSongsFragmentViewModel>()

    private var _binding: FragmentFavSongsBinding? = null
    private val binding get() = _binding!!

    private lateinit var noDataFrame: FrameLayout
    private lateinit var recyclerView: RecyclerView
    private val data = ArrayList<Track>()

    private var isClickAllowed: Boolean = true
    private fun isClickAllowed(): Boolean {
        val startState = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return startState
    }

    private fun switchToPlayer() {
        val mediaIntent = Intent(context, MediaActivity::class.java)
        startActivity(mediaIntent)
    }

    private val trackViewHolderItemClicked: (Track) -> Unit = { item ->
        run {
            if (isClickAllowed()) {
                viewmodel.saveCurrentlyPlaying( item )
                switchToPlayer()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.loadDBFavoriteTracks()
    }
/*    override fun onStart() {
        super.onStart()
        viewmodel.loadDBFavoriteTracks()
    }*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noDataFrame = binding.noDataInfo

        recyclerView = binding.favSongsRView
        recyclerView.adapter = RecyclerViewTrackAdapter(
            data, trackViewHolderItemClicked
        )

        viewmodel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {

                is FavSongsFragmentScreenUpdate.DoNothing -> {
                    //
                }

                is FavSongsFragmentScreenUpdate.ShowNoData -> {

                    noDataFrame.visibility = View.VISIBLE

                    val animate = TranslateAnimation(
                        0f, 0f,
                        noDataFrame.height.toFloat(), 0f
                    ).apply {
                        duration = 500
                        fillAfter = true
                    }

                    noDataFrame.startAnimation(animate)

                    data.clear()
                    recyclerView.adapter?.notifyDataSetChanged()

                    viewmodel.updateRecieved()
                }

                is FavSongsFragmentScreenUpdate.DBFavoriteTracks -> {

                    noDataFrame.visibility = View.GONE
                    data.clear()
                    data.addAll( state.tracks )
                    recyclerView.adapter?.notifyDataSetChanged()

                    viewmodel.updateRecieved()
                }

            }
        }

    }
}