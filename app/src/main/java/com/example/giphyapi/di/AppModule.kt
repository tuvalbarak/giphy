package com.example.giphyapi.di

import com.example.giphyapi.remote.GifService
import com.example.giphyapi.remote.GifServiceHelper
import com.example.giphyapi.remote.GifServiceHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideBaseUrl() = "https://api.giphy.com/"

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideGifService(retrofit: Retrofit): GifService = retrofit.create(GifService::class.java)

    @Singleton
    @Provides
    fun provideGifServiceHelper(gifServiceHelper: GifServiceHelperImpl): GifServiceHelper = gifServiceHelper

}