package com.example.playlistmaker.data.web


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.example.playlistmaker.data.DTO.GeneralResponse
import com.example.playlistmaker.data.DTO.RequestData
import com.example.playlistmaker.data.api.NetworkClient
import com.example.playlistmaker.data.api.SearchAPIService



class RetrofitNetworkClient(
    private val searchAPIService: SearchAPIService,
    private val context: Context
) : NetworkClient {

    /*private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()*/

    //private val searchAPIService = retrofit.create<SearchAPIService>(SearchAPIService::class.java)

    private fun isInternetAvailable(): Boolean {

        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    override suspend fun makeRequest(requestObject: Any): GeneralResponse {

        if (!isInternetAvailable()) {
            return GeneralResponse().apply { responseCode = -1 }
        }

        when (requestObject) {

            is RequestData -> {

                return withContext(Dispatchers.IO) {
                    try {

                        searchAPIService.getTracksByTerm(requestObject.requestText)
                            .apply { responseCode = 200 }

                    } catch (error: Throwable) {

                        GeneralResponse()
                            .apply { responseCode = 500 }

                    }
                }
                /*val response = searchAPIService.getTracksByTerm(requestObject.requestText)
                val respBody = response.body() ?: GeneralResponse()
                return respBody.apply { responseCode = response.code() }*/
            }

            else -> {
                return GeneralResponse().apply { responseCode = 400 }
            }
        }
    }

}