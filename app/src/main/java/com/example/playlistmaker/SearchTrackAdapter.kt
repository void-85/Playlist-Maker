package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SearchTrackAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<SearchTrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return SearchTrackViewHolder(view)
    }


    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {

        holder.bind(tracks[position])
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim)
    }


    override fun getItemCount() :Int = tracks.size
}