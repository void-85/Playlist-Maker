package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.PlaylistsFragmentScreenUpdate
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.fragsHolderActivity.viewHolderAdapter.RecyclerViewPlaylistAdapter
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


    private lateinit var recyclerView: RecyclerView
    private var data = ArrayList<Playlist>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



/*        data.add(Playlist(0L,"test", "", "1703473002", emptyList(), 15))
        data.add(Playlist(0L,"tsaasdasdasdasdasdasdasdasdasdasdasdasdasdasdest", "", "", emptyList(), 15))
        data.add(Playlist(0L,"test", "", "1703473002", emptyList(), 15))
        data.add(Playlist(0L,"test", "", "1703473002", emptyList(), 15))
        data.add(Playlist(0L,"test", "", "1703473002", emptyList(), 15))
        data.add(Playlist(0L,"test", "", "1703473002", emptyList(), 15))
        data.add(Playlist(0L,"test", "", "1703473002", emptyList(), 15))
        data.add(Playlist(0L,"test", "", "1703473002", emptyList(), 15))
        data.add(Playlist(0L,"test", "", "1703473002", emptyList(), 15))*/



        recyclerView = binding.playlistsRView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = RecyclerViewPlaylistAdapter(data)






        viewmodel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsFragmentScreenUpdate.ShowNoData -> {

                    binding.noDataInfo.visibility = View.VISIBLE

                    val animate = TranslateAnimation(
                        0f, 0f,
                        binding.noDataInfo.height.toFloat() * 2, 0f
                    ).apply {
                        duration = 500
                        fillAfter = true
                    }
                    binding.noDataInfo.startAnimation(animate)
                }
            }
        }
        //binding.playlistsFragText.text = getString( requireArguments().getInt(TEXT_RESOURCE))
    }
}