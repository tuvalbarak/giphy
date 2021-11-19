package com.example.giphyapi

import android.app.Application
import com.example.giphyapi.remote.RetrofitFactory
import com.example.giphyapi.repos.RepoFactory

/**
 * Initializer will be called when the app is created, it contains necessary data in order to start the app.
 */
object Initializer {

    fun init(application: Application) {
        //Initializing the app context
        RepoFactory.context = application
        //Creating the required Retrofit services
        RetrofitFactory.configure()
    }
}