package com.example.playlistmaker.ui.fragsHolderActivity.viewHolderAdapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track


class RecyclerViewTrackAdapter(

    private val tracks: List<Track>,
    private val trackViewHolderItemClicked: (Track) -> Unit

) : RecyclerView.Adapter<RecyclerViewTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewTrackViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return RecyclerViewTrackViewHolder(
            view,
            trackViewHolderItemClicked
        )
    }

    override fun onBindViewHolder(holder: RecyclerViewTrackViewHolder, position: Int) {

        holder.bind(tracks[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(
                holder.itemView.context, R.anim.anim
            )
    }

    override fun getItemCount(): Int = tracks.size
}