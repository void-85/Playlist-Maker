package com.example.playlistmaker


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson



class SearchTrackViewHolder( itemView :View ) :RecyclerView.ViewHolder(itemView) {

    private val trackName  :TextView  = itemView.findViewById( R.id.track_view_track_name  )
    private val artistName :TextView  = itemView.findViewById( R.id.track_view_artist_name )
    private val trackTime  :TextView  = itemView.findViewById( R.id.track_view_track_time  )
    private val artworkUrl :ImageView = itemView.findViewById( R.id.track_view_artwork_url )


    fun bind( model :Track ){

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
                    itemView.resources.displayMetrics.density.toInt() *
                    itemView.resources.getInteger(R.integer.search_track_view_artwork_corner_radius)
                )
            )
            .into(artworkUrl)


        // ? excess amount of listeners
        itemView.setOnClickListener {
            //Toast.makeText(itemView.context, "${model.trackName} clicked", Toast.LENGTH_LONG).show()

            // swap old items
            if (history_data.contains(model)) {

                val oldPos: Int = history_data.indexOf(model)
                history_data.remove(model)
                history_data.add(0, model)
                history_rView.adapter?.notifyItemMoved(oldPos, 0)
                history_rView.scrollToPosition(0)

                // insert new item
            } else {

                history_data.add(0, model)
                history_rView.adapter?.notifyItemInserted(0)
                history_rView.scrollToPosition(0)

                if (history_data.size > 10) {

                    history_data.removeAt(10)
                    history_rView.adapter?.notifyItemRemoved(10)

                }
            }

            isSearchHistoryEmpty = false

            sharedPrefs
                .edit()
                .putString(
                    App.SEARCH_HISTORY_KEY,
                    Gson().toJson(history_data)
                )
                .putBoolean(
                    App.IS_SEARCH_HISTORY_EMPTY,
                    false
                )
                .apply()
        }
    }
}