package com.example.giphyapi.repos

import com.example.giphyapi.remote.GifServiceHelper
import javax.inject.Inject

/**
 * The Repo contains suspend functions to enable async performance using Coroutines.
 */

class GifRepo @Inject constructor (private val gifServiceHelper: GifServiceHelper) {
    private val API_KEY = "Zz7XnA0RZzJJetQAQv1e2c7ErivA9F5u"

    suspend fun getGifsByKeyword(keyword: String) =
        gifServiceHelper.getGifsByKeyword(keyword, API_KEY).body()?.data

    suspend fun getTrendingGifs() =
        gifServiceHelper.getTrendingGifs(API_KEY).body()?.data
}