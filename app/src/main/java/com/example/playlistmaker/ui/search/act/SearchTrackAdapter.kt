package com.example.playlistmaker.ui.search.act

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.search.vm.SearchActivityViewModel

class SearchTrackAdapter(
    private val tracks: List<Track>,
    private val switchActivityAction: () -> (Unit),
    private val saveSearchHistoryAndCurrentlyPlayingFun: (List<Track>, Track) -> Unit,

    private val historyRView: RecyclerView,
    private val historyData: ArrayList<Track>,
    private var isSearchHistoryEmpty: Boolean,
    private val viewModel: SearchActivityViewModel

) : RecyclerView.Adapter<SearchTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return SearchTrackViewHolder(
            view,
            switchActivityAction,
            saveSearchHistoryAndCurrentlyPlayingFun,
            historyRView,
            historyData,
            isSearchHistoryEmpty,
            viewModel
        )
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {

        holder.bind(tracks[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(
                holder.itemView.context, R.anim.anim
            )
    }

    override fun getItemCount(): Int = tracks.size
}