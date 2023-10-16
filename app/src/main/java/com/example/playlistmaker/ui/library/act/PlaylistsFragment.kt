package com.example.playlistmaker.ui.library.act


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding


class PlaylistsFragment : Fragment() {

    companion object {
        private const val TEXT_RESOURCE = "text"

        fun newInstance(textResource: Int) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putInt(TEXT_RESOURCE, textResource)
            }
        }
    }

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playlistsFragText.text = getString( requireArguments().getInt(TEXT_RESOURCE))
    }
}