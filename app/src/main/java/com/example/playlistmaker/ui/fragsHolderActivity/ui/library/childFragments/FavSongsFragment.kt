package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments


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
    private lateinit var binding: FragmentFavSongsBinding

    private lateinit var noDataFrame: FrameLayout
    private lateinit var recyclerView: RecyclerView
    private var data = ArrayList<Track>()

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

    private val trackViewHolderItemClicked: (Track) -> Unit = { item ->
        run {}
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavSongsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {

                is FavSongsFragmentScreenUpdate.ShowNoData -> {

                    noDataFrame.visibility = View.VISIBLE
                    val animate = TranslateAnimation(
                        0f, 0f,
                        binding.noDataInfo.height.toFloat(), 0f
                    ).apply {
                        duration = 500
                        fillAfter = true
                    }
                    binding.noDataInfo.startAnimation(animate)
                }

                is FavSongsFragmentScreenUpdate.DBFavoriteTracks -> {

                    noDataFrame.visibility = View.GONE
                    data.clear()
                    data.addAll( state.tracks )
                    recyclerView.adapter?.notifyDataSetChanged()
                }

            }
        }
        //binding.favSongFragText.text = getString(requireArguments().getInt(TEXT_RESOURCE))

        noDataFrame = binding.noDataInfo

        recyclerView = binding.favSongsRView
        recyclerView.adapter = RecyclerViewTrackAdapter(
            data, trackViewHolderItemClicked
        )
    }
}