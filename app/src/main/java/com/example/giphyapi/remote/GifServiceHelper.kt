package com.example.giphyapi.remote

import com.example.giphyapi.models.GifResponse
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

interface GifServiceHelper {

    suspend fun getGifsByKeyword(
        @Query("q") keyword: String,
        @Query("api_key") apiKey: String
    ): Response<GifResponse>

    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String
    ): Response<GifResponse>
}

class GifServiceHelperImpl @Inject constructor (private val gifService: GifService) : GifServiceHelper {

    override suspend fun getGifsByKeyword(keyword: String, apiKey: String) =
        gifService.getGifsByKeyword(keyword, apiKey)

    override suspend fun getTrendingGifs(apiKey: String) =
        gifService.getTrendingGifs(apiKey)
}