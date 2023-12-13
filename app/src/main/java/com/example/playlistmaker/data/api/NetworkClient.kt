package com.example.playlistmaker.data.api


import com.example.playlistmaker.data.DTO.GeneralResponse


interface NetworkClient {

    suspend fun makeRequest(requestObject: Any): GeneralResponse

}