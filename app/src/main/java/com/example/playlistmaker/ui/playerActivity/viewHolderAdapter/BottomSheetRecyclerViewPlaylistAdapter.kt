package com.example.playlistmaker.ui.playerActivity.viewHolderAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Playlist

class BottomSheetRecyclerViewPlaylistAdapter(
    private val playlists: List<Playlist>
) : RecyclerView.Adapter<BottomSheetRecyclerViewPlaylistViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetRecyclerViewPlaylistViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.bottom_sheet_playlist_view,
                parent,
                false
            )
        return BottomSheetRecyclerViewPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BottomSheetRecyclerViewPlaylistViewHolder,
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