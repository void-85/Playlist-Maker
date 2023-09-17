package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.DTO.RequestData
import com.example.playlistmaker.data.DTO.ResponseData
import com.example.playlistmaker.data.api.NetworkClient
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.presentation.presenters.millisToMinSec

class TracksRepositoryImpl( private val networkClient : NetworkClient) : TracksRepository {

    override fun searchTracks(searchText: String): List<Track> {
        val response = networkClient.makeRequest( RequestData( searchText ) )
        when(response.responseCode){

            200  -> {
                return (response as ResponseData).results.map {
                    Track(
                        trackName     = it.trackName            ,
                        artistName    = it.artistName           ,
                        artworkUrl100 = it.artworkUrl100        ,
                        trackTime     = it.trackTimeMillis
                                            .millisToMinSec()   ,

                        collectionName    = it.collectionName   ,
                        releaseDate       = it.releaseDate      ,
                        primaryGenreName  = it.primaryGenreName ,
                        country           = it.country          ,

                        previewUrl        = it.previewUrl       )
                }
            }

            else -> { return emptyList() }
        }
    }

}