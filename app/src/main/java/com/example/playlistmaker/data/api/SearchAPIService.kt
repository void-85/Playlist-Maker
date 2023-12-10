package com.example.playlistmaker.data.api


import com.example.playlistmaker.data.DTO.ResponseData
import retrofit2.http.GET
import retrofit2.http.Query


interface SearchAPIService {

    @GET("/search?entity=song")
    suspend fun getTracksByTerm(@Query("term") text: String): ResponseData

}