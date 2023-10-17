package com.example.playlistmaker.ui.library.act.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavSongsBinding


class FavSongsFragment : Fragment() {

    companion object {
        private const val TEXT_RESOURCE = "text"

        fun newInstance(textResource: Int) = FavSongsFragment().apply {
            arguments = Bundle().apply {
                putInt(TEXT_RESOURCE, textResource)
            }
        }
    }

    private lateinit var binding: FragmentFavSongsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favSongFragText.text = getString(requireArguments().getInt(TEXT_RESOURCE))
    }
}