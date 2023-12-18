package com.example.playlistmaker.data.repositories


import com.example.playlistmaker.data.DTO.RequestData
import com.example.playlistmaker.data.DTO.ResponseData
import com.example.playlistmaker.data.api.NetworkClient
import com.example.playlistmaker.data.utils.millisToMinSec
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.api.repositories.TracksRepository
import com.example.playlistmaker.domain.db.FavTracksRepository
import com.example.playlistmaker.ui.utils.Option

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val favTracksRepositoryImpl: FavTracksRepository
) : TracksRepository {

    override fun searchTracks(searchText: String): Flow<Option<List<Track>>> = flow {

        val response = networkClient.makeRequest(RequestData(searchText))

        when (response.responseCode) {

            -1 -> emit(Option.Error(null, "NO INTERNET"))

            200 -> {
                with(response as ResponseData) {

                    val favIds = ArrayList<Long>()
                    favTracksRepositoryImpl.getAllTracksIds().collect{
                        favIds.add(it)
                    }

                    //Log.d(">>>","during search : ${favIds.size} fav ids of fav tracks fetched")

                    emit(Option.Data(response.results.map {
                        Track(
                            trackId = it.trackId,

                            trackName = it.trackName ?: "-",
                            artistName = it.artistName ?: "-",
                            artworkUrl100 = it.artworkUrl100 ?: "-",
                            trackTime = it.trackTimeMillis?.millisToMinSec() ?: "-:--",

                            collectionName = it.collectionName ?: "-",
                            releaseDate = it.releaseDate ?: "-",
                            primaryGenreName = it.primaryGenreName ?: "-",
                            country = it.country ?: "-",

                            previewUrl = it.previewUrl ?: "-",

                            isFavorite = favIds.contains(it.trackId)
                        )
                    }))
                }
            }

            else -> emit(Option.Error(null, "OTHER ERROR"))
        }
    }

}