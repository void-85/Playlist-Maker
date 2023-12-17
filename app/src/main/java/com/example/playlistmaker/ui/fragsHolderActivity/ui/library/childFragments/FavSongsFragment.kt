package com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.playlistmaker.databinding.FragmentFavSongsBinding
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.FavSongsFragmentScreenUpdate
import com.example.playlistmaker.ui.fragsHolderActivity.ui.library.childFragmentsVM.FavSongsFragmentViewModel


class FavSongsFragment : Fragment() {

    companion object {
        //private const val TEXT_RESOURCE = "text"
        fun newInstance() = FavSongsFragment().apply {
            /*arguments = Bundle().apply {
                putInt(TEXT_RESOURCE, textResource)
            }*/
        }
    }

    private val viewmodel by viewModel<FavSongsFragmentViewModel>()
    private lateinit var binding: FragmentFavSongsBinding


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

                    binding.noDataInfo.visibility = View.VISIBLE
                    val animate = TranslateAnimation(
                        0f, 0f,
                        binding.noDataInfo.height.toFloat(), 0f
                    ).apply {
                        duration = 500
                        fillAfter = true
                    }
                    binding.noDataInfo.startAnimation(animate)
                }


            }
        }
        //binding.favSongFragText.text = getString(requireArguments().getInt(TEXT_RESOURCE))
    }
}