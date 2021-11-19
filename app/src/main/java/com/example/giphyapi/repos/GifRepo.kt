package com.example.giphyapi.repos

import com.example.giphyapi.models.Gif
import com.example.giphyapi.remote.gifsApi

/**
 * The Repo contains suspend functions to enable async performance using Coroutines.
 */
interface GifRepo {
    suspend fun getGifsByKeyword(keyword: String): List<Gif>?
    suspend fun getTrendingGifs(): List<Gif>?
}

internal object GifRepoImpl : GifRepo {

    private const val API_KEY = "Zz7XnA0RZzJJetQAQv1e2c7ErivA9F5u"

    override suspend fun getGifsByKeyword(keyword: String) =
        gifsApi.getGifsByKeyword(keyword, API_KEY).body()?.data

    override suspend fun getTrendingGifs() =
        gifsApi.getTrendingGifs(API_KEY).body()?.data

}