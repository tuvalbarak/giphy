package com.example.giphyapi.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giphyapi.models.Gif
import com.example.giphyapi.repos.GifRepo
import com.example.giphyapi.utils.States
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifViewModel @Inject constructor (private val gifRepo: GifRepo) : ViewModel() {

    //Holds the state of the app.
    val state = MutableLiveData<States>().apply {
        postValue(States.Idle)
    }

    /**
     * gifList will initially hold the trending gifs. After a search is performed - it will hold the relevant gifs according to the search. If the search
     * box is empty again - it will hold the trending gifs.
     */
    val gifList = MutableLiveData<List<Gif>>().apply {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            postValue(gifRepo.getTrendingGifs())
            state.postValue(States.Idle)
        }
    }

    //If the query is empty - fetch the trending gifs. Otherwise - fetch gifs according to the query.
    fun onQueryChanged(query: String) {
        Log.d("yoyo", "onQueryChaned - $query")
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(States.Loading)
            val gifs = if(query.isEmpty()) gifRepo.getTrendingGifs() else gifRepo.getGifsByKeyword(query)
            gifList.postValue(gifs)
            state.postValue(States.Idle)
        }
    }
}