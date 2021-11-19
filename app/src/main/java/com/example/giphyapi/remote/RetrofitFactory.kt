package com.example.giphyapi.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal lateinit var gifsApi: GifService

/**
 * Implemented Retrofit creation as a Singleton, Used the Factory design pattern to achieve reuse of my code scalability.
 * Implemented a generic create function to support code reuse and flexibility.
 */
object RetrofitFactory {

    private const val baseUrl = "https://api.giphy.com/"
    private var retrofit: Retrofit? = null

    //Whenever new endpoint will be added - it has to be initialized inside configure.
    fun configure() {
        gifsApi = create(GifService::class.java)
    }

    private fun <T> create(service: Class<T>): T = retrofit?.create(service) ?: run {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit!!.create(service)
    }

}