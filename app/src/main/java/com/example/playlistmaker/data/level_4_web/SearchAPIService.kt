package com.example.playlistmaker.data.level_4_web


import com.example.playlistmaker.data.DTO.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface SearchAPIService {

    @GET("/search?entity=song")
    fun getTracksByTerm( @Query("term") text :String ) :Call<ResponseData>

}