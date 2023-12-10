package com.example.playlistmaker.data.DTO

data class ResponseData(
    val resultCount : Long,
    val results     : List<TrackDTO>
) : GeneralResponse()
