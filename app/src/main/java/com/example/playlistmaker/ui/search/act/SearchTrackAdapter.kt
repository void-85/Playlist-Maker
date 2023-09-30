package com.example.playlistmaker.ui.search.act

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track

class SearchTrackAdapter(
    private val tracks: List<Track>,
    private val switchActivityAction: () -> (Unit)
) : RecyclerView.Adapter<SearchTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return SearchTrackViewHolder(view, switchActivityAction)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {

        holder.bind(tracks[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(
                holder.itemView.context, R.anim.anim
            )
    }

    override fun getItemCount() :Int = tracks.size
}