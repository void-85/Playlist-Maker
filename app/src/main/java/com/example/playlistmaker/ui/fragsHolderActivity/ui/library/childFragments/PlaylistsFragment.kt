package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.PlaylistsFragmentScreenUpdate
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.viewHolderAdapters.RecyclerViewPlaylistAdapter
import com.example.playlistmaker.ui.newPlaylistActivity.act.NewPlaylistActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment : Fragment() {

    companion object {
        //private const val TEXT_RESOURCE = "text"
        fun newInstance() = PlaylistsFragment().apply {
            /*arguments = Bundle().apply {
                putInt(TEXT_RESOURCE, textResource)
            }*/
        }
    }

    private val viewmodel by viewModel<PlaylistsFragmentViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!



    private var data = ArrayList<Playlist>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var noDataFrame: FrameLayout
    private lateinit var createPlaylistButton: Button






    private fun showNoPlaylists() {
        recyclerView.visibility = View.INVISIBLE
        noDataFrame.visibility = View.VISIBLE

        val animate = TranslateAnimation(
            0f, 0f,
            noDataFrame.height.toFloat() * 2, 0f
        ).apply {
            duration = 500
            fillAfter = true
        }
        noDataFrame.startAnimation(animate)
    }

    private fun showPlaylists() {
        recyclerView.visibility = View.VISIBLE
        noDataFrame.visibility = View.INVISIBLE
    }

    private fun showNothing() {
        recyclerView.visibility = View.INVISIBLE
        noDataFrame.visibility = View.INVISIBLE
    }





    override fun onResume() {
        super.onResume()
        viewmodel.requestAllPlaylists()
    }

    override fun onPause() {
        super.onPause()
        showNothing()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.playlistsRView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = RecyclerViewPlaylistAdapter(data)

        noDataFrame = binding.noDataInfo

        createPlaylistButton = binding.createNewPlaylistButton
        createPlaylistButton.setOnClickListener {
            val newPlaylistIntent = Intent(context, NewPlaylistActivity::class.java)
            startActivity(newPlaylistIntent)
        }




        viewmodel.getState().observe(viewLifecycleOwner) {
            when (it) {

                // DELETE STATE??
                is PlaylistsFragmentScreenUpdate.ShowNoData -> {
                    showNoPlaylists()
                }

                is PlaylistsFragmentScreenUpdate.ShowAllPlaylists -> {

                    //Toast.makeText( context, "${it.playlists.size} playlists extracted from db", Toast.LENGTH_LONG).show()
                    if (it.playlists.isEmpty()) {

                        showNoPlaylists()

                    } else {

                        showPlaylists()

                        data.clear()
                        data.addAll(it.playlists)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
        //binding.playlistsFragText.text = getString( requireArguments().getInt(TEXT_RESOURCE))
    }
}