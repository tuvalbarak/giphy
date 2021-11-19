package com.example.giphyapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.giphyapi.Initializer
import com.example.giphyapi.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Calling the init function that will perform initial actions for running the app.
        Initializer.init(application)
        setContentView(R.layout.activity_main)
    }
}