package com.example.giphyapi.remote

import com.example.giphyapi.models.GifResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Contains the gifs possible endpoints
 */
interface GifService {

    //Returns gifs by keyword.
    @GET("v1/gifs/search")
    suspend fun getGifsByKeyword(
        @Query("q") keyword: String,
        @Query("api_key") apiKey: String
    ): Response<GifResponse>

    //Returns current trending gifs.
    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String
    ): Response<GifResponse>
}