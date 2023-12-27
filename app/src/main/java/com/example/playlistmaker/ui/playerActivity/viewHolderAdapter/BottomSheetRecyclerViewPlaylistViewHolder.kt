package com.example.playlistmaker.ui.playerActivity.viewHolderAdapter

import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.File

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.utils.toTrackNumberString


class BottomSheetRecyclerViewPlaylistViewHolder(
    itemView: View,
    private val playlistClicked: (Playlist) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView = itemView.findViewById(R.id.image)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val tracksCount: TextView = itemView.findViewById(R.id.number_of_tracks)

    fun bind(model :Playlist){

        name.text = model.name
        tracksCount.text = model.numberOfTracks.toTrackNumberString()

        var uri : Uri? = null
        if( model.imageId != "" ){

            val filePath = File( itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlists")
            val file = File(filePath, model.imageId)
            uri = file.toUri()
        }

        Glide
            .with(itemView)
            .load(uri)
            .placeholder(R.drawable.spiral)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(
                        R.dimen.new_playlist_upload_image_corner_radius
                    ))
            )
            .into(image)

        itemView.setOnClickListener {
            playlistClicked(model)
        }
    }
}