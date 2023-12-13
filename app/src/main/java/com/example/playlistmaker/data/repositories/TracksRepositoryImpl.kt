package com.example.playlistmaker.data.repositories


import com.example.playlistmaker.data.DTO.RequestData
import com.example.playlistmaker.data.DTO.ResponseData
import com.example.playlistmaker.data.api.NetworkClient
import com.example.playlistmaker.data.utils.millisToMinSec
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.api.repositories.TracksRepository
import com.example.playlistmaker.ui.utils.Option

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override fun searchTracks(searchText: String): Flow<Option<List<Track>>> = flow {

        val response = networkClient.makeRequest(RequestData(searchText))

        when (response.responseCode) {

            -1 -> emit(Option.Error(null, "NO INTERNET"))

            200 -> {
                with(response as ResponseData) {

                    emit(Option.Data(response.results.map {
                        Track(
                            trackName = it.trackName ?: "-",
                            artistName = it.artistName ?: "-",
                            artworkUrl100 = it.artworkUrl100 ?: "-",
                            trackTime = it.trackTimeMillis?.millisToMinSec() ?: "-:--",

                            collectionName = it.collectionName ?: "-",
                            releaseDate = it.releaseDate ?: "-",
                            primaryGenreName = it.primaryGenreName ?: "-",
                            country = it.country ?: "-",

                            previewUrl = it.previewUrl ?: "-"
                        )
                    }))

                    /*                    response.results.map {
                                            emit(Option.Data( listOf(Track(
                                                trackName = it.trackName ?: "-",
                                                artistName = it.artistName ?: "-",
                                                artworkUrl100 = it.artworkUrl100 ?: "-",
                                                trackTime = it.trackTimeMillis?.millisToMinSec() ?: "-:--",

                                                collectionName = it.collectionName ?: "-",
                                                releaseDate = it.releaseDate ?: "-",
                                                primaryGenreName = it.primaryGenreName ?: "-",
                                                country = it.country ?: "-",

                                                previewUrl = it.previewUrl ?: "-"
                                            ))
                                            ))*/

                    /*return (response as ResponseData).results.map {
                        Track(
                            trackName     = it.trackName ?: "-",
                            artistName    = it.artistName ?: "-",
                            artworkUrl100 = it.artworkUrl100 ?: "-",
                            trackTime     = it.trackTimeMillis?.millisToMinSec() ?: "-:--",
                                //.millisToMinSec() ?: 0L,

                            collectionName   = it.collectionName ?: "-",
                            releaseDate      = it.releaseDate ?: "-",
                            primaryGenreName = it.primaryGenreName ?: "-",
                            country          = it.country ?: "-",

                            previewUrl = it.previewUrl ?: "-"
                        )*/
                }
            }

            else -> emit(Option.Error(null, "OTHER ERROR"))
        }
    }

}