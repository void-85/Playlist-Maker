package com.example.playlistmaker


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners



class SearchTrackViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    private val trackName  :TextView  = itemView.findViewById( R.id.track_view_track_name  )
    private val artistName :TextView  = itemView.findViewById( R.id.track_view_artist_name )
    private val trackTime  :TextView  = itemView.findViewById( R.id.track_view_track_time  )
    private val artworkUrl :ImageView = itemView.findViewById( R.id.track_view_artwork_url )

    fun bind( model :Track ){

        trackName.text  = model.trackName
        artistName.text = model.artistName
        trackTime.text  = model.trackTime

        Glide
            .with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.spiral)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    itemView.resources.displayMetrics.density.toInt() *
                    itemView.resources.getInteger( R.integer.search_track_view_artwork_corner_radius )
                )
            )
            .into(artworkUrl)
    }
}