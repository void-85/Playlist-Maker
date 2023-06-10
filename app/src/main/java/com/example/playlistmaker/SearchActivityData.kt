package com.example.playlistmaker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners



data class Track( val trackName     :String ,
                  val artistName    :String ,
                  val trackTime     :String ,
                  val artworkUrl100 :String )



class TrackViewHolder( itemView : View) :RecyclerView.ViewHolder(itemView){

    private val trackName  :TextView  = itemView.findViewById( R.id.track_view_track_name  )
    private val artistName :TextView  = itemView.findViewById( R.id.track_view_artist_name )
    private val trackTime  :TextView  = itemView.findViewById( R.id.track_view_track_time  )
    private val artworkUrl :ImageView = itemView.findViewById( R.id.track_view_artwork_url )

    fun bind( model :Track ){

         trackName.text = model.trackName
        artistName.text = model.artistName
         trackTime.text = model.trackTime

        val dp2px :Int = (Resources.getSystem().displayMetrics.density *2 ).toInt()

        Glide
            .with       ( itemView                )
            .load       ( model.artworkUrl100     )
            .placeholder( R.drawable.spiral       )
            .transform  ( CenterCrop()            ,
                          RoundedCorners(dp2px)   )
            .into       ( artworkUrl              )
    }
}



class TrackAdapter( private val tracks :List<Track> ) :RecyclerView.Adapter<TrackViewHolder>(){

    override fun onCreateViewHolder( parent :ViewGroup, viewType :Int ) :TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder( holder :TrackViewHolder, position :Int) {
        holder.bind( tracks[position] )

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim)
    }

    override fun getItemCount() :Int {
        return tracks.size
    }
}



class SearchActivityMockData {

    private val mockData = arrayListOf<Track>(

        // TEST TRACK WITH LONG STRINGS
        Track(  trackName     = "Nyan caaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaat",
                artistName    = "Nyan caaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaat",
                trackTime     = "1234:56",
                artworkUrl100 = "https://broken.link/src.jpg" ),

        Track(  trackName     = "Smells Like Teen Spirit",
                artistName    = "Nirvana",
                trackTime     = "5:01",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg" ),

        Track(  trackName     = "Billie Jean",
                artistName    = "Michael Jackson",
                trackTime     = "4:35",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg" ),

        Track(  trackName     = "Stayin' Alive",
                artistName    = "Bee Gees",
                trackTime     = "4:10",
                artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg" ),

        Track(  trackName     = "Whole Lotta Love",
                artistName    = "Led Zeppelin",
                trackTime     = "5:33",
                artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg" ),

        Track(  trackName     = "Sweet Child O'Mine",
                artistName    = "Guns N' Roses",
                trackTime     = "5:03",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg" )
    )



    fun getData() :ArrayList<Track> { return mockData }

}