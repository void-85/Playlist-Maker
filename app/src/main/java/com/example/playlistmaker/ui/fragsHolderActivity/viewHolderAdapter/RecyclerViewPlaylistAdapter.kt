package com.example.playlistmaker.ui.fragsHolderActivity.viewHolderAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Playlist

class RecyclerViewPlaylistAdapter(
    private val playlists: List<Playlist>
) : RecyclerView.Adapter<RecyclerViewPlaylistViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewPlaylistViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.playlist_view,
                parent,
                false
            )
        return RecyclerViewPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerViewPlaylistViewHolder,
        position: Int
    ) {
        holder.bind( playlists[position] )
        holder.itemView.animation=
            AnimationUtils.loadAnimation(
                holder.itemView.context, R.anim.anim
            )
    }

    override fun getItemCount(): Int = playlists.size
}