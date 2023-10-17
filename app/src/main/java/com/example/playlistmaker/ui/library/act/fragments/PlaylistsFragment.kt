package com.example.playlistmaker.ui.library.act.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.library.vm.PlaylistsFragmentScreenUpdate
import com.example.playlistmaker.ui.library.vm.PlaylistsFragmentViewModel


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
    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsFragmentScreenUpdate.ShowNoData -> {
                    binding.noDataInfo.visibility = View.VISIBLE
                }
            }
        }
        //binding.playlistsFragText.text = getString( requireArguments().getInt(TEXT_RESOURCE))
    }
}