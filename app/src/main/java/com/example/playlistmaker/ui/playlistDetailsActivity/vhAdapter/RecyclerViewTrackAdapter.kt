package com.example.playlistmaker.ui.playlistDetailsActivity.vhAdapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track


class BottomSheetRecyclerViewTrackAdapter(

    private val tracks: List<Track>,
    private val trackViewHolderItemClicked: (Track) -> Unit,
    private val trackLongTouched: (Track) -> Unit

) : RecyclerView.Adapter<BottomSheetRecyclerViewTrackViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetRecyclerViewTrackViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.track_view,
                parent,
                false
            )
        return BottomSheetRecyclerViewTrackViewHolder(
            view,
            trackViewHolderItemClicked,
            trackLongTouched
        )
    }

    override fun onBindViewHolder(
        holder: BottomSheetRecyclerViewTrackViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(
                holder.itemView.context, R.anim.anim
            )
    }

    override fun getItemCount(): Int = tracks.size
}