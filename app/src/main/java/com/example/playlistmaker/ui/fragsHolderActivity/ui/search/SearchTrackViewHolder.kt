package com.example.playlistmaker.ui.fragsHolderActivity.ui.search


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track


class SearchTrackViewHolder(
    itemView: View,
    private val trackViewHolderItemClicked: (Track, Boolean, Runnable, Runnable) -> Unit

) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.track_view_track_name)
    private val artistName: TextView = itemView.findViewById(R.id.track_view_artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_view_track_time)
    private val artworkUrl: ImageView = itemView.findViewById(R.id.track_view_artwork_url)

    private var isClickAllowed = true
    private val enableClick = Runnable { isClickAllowed = true }
    private val disableClick = Runnable { isClickAllowed = false }


    fun bind(model: Track) {

        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

        Glide
            .with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.spiral)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(
                        R.dimen.search_track_view_artwork_corner_radius
                    )
                )
            )
            .into(artworkUrl)

        itemView.setOnClickListener {
            trackViewHolderItemClicked(model, isClickAllowed, enableClick, disableClick)
        }
    }
}