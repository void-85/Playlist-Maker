package com.example.playlistmaker.data.api


import com.example.playlistmaker.data.DTO.GeneralResponse


interface NetworkClient {

    fun makeRequest( requestObject :Any ) : GeneralResponse

}