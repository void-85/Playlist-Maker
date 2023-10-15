package com.example.playlistmaker.ui.library.act


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding


class PlaylistsFragment : Fragment() {

    companion object {
        private const val TEXT_KEY = "text"

        fun newInstance(text: String) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString(TEXT_KEY, text)
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
        binding.playlistsFragText.text = requireArguments().getString(TEXT_KEY)
    }
}