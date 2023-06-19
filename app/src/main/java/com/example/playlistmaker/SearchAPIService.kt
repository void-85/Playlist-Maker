package com.example.playlistmaker

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAPIService {

    @GET("/search?entity=song")
    fun getTracksByTerm( @Query("term") text :String ) :Call<ResponseData>

}