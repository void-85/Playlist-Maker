package com.example.playlistmaker.data.level_4_web

import com.example.playlistmaker.data.DTO.GeneralResponse
import com.example.playlistmaker.data.DTO.RequestData
import com.example.playlistmaker.data.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchAPIService = retrofit.create<SearchAPIService>(SearchAPIService::class.java)



    override fun makeRequest( requestObject :Any ) : GeneralResponse {

        when(requestObject){

            is RequestData -> {

                    val response = searchAPIService
                                    .getTracksByTerm( requestObject.requestText )
                                    .execute()

                    val respBody = response.body() ?: GeneralResponse()

                    return respBody.apply{ responseCode = response.code() }
            }

            else -> { return GeneralResponse().apply{ responseCode = 400 } }
        }

    }

}