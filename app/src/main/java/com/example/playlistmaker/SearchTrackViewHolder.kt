package com.example.playlistmaker


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson


class SearchTrackViewHolder( itemView :View, private val switchActivity: ()->(Unit) ) :RecyclerView.ViewHolder(itemView) {


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
                    itemView.resources.getDimensionPixelSize(
                        R.dimen.search_track_view_artwork_corner_radius
                    )
                )
            )
            .into(artworkUrl)


        // ? excess amount of listeners
        itemView.setOnClickListener {
            //Toast.makeText(itemView.context, "${model.trackName} clicked", Toast.LENGTH_LONG).show()

            // swap old items
            if (historyData.contains(model)) {

                val oldPos: Int = historyData.indexOf(model)
                historyData.remove(model)
                historyData.add(0, model)

                historyRView.adapter?.notifyItemMoved(oldPos, 0)
                historyRView.scrollToPosition(0)

            // insert new item
            } else {

                historyData.add(0, model)

                historyRView.adapter?.notifyItemInserted(0)
                historyRView.scrollToPosition(0)

                if (historyData.size > App.SEARCH_HISTORY_MAX_LENGTH) {

                    historyData.removeAt(App.SEARCH_HISTORY_MAX_LENGTH)
                    historyRView.adapter?.notifyItemRemoved(App.SEARCH_HISTORY_MAX_LENGTH)

                }
            }

            isSearchHistoryEmpty = false

            sharedPrefs
                .edit()
                .putString ( App.SEARCH_HISTORY_KEY      , Gson().toJson(historyData) )
                .putBoolean( App.IS_SEARCH_HISTORY_EMPTY , false                      )
                .putString ( App.CURRENTLY_PLAYING_KEY   , Gson().toJson(model)       )
                .apply()

            switchActivity()
        }
    }
}