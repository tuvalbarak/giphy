package com.example.giphyapi.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.giphyapi.repos.RepoFactory

/**
 * Using the Factory design pattern for a scalable solution to VM approach.
 */
object ViewModelFactory {
    fun create(context: Context) : ViewModelProvider.AndroidViewModelFactory =
        ViewModelFactoryImpl(context.applicationContext as Application)
}

@Suppress("UNCHECKED_CAST")
private class ViewModelFactoryImpl(val app: Application) : ViewModelProvider.AndroidViewModelFactory(app) {
    //Whenever new VMs will be needed - they just need to be added to this function.
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when(modelClass) {
        GifViewModel::class.java -> GifViewModel(RepoFactory.gifRepo, app) as T
        else -> throw NotImplementedError(modelClass.toString())
    }
}