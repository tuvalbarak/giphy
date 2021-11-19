package com.example.giphyapi.repos

import android.app.Application

/**
 * Using the Factory design pattern to create a scalable solution that can maintain any
 * number of different repositories across the app.
 */
object RepoFactory {
    lateinit var context: Application
    val gifRepo: GifRepo = GifRepoImpl
}