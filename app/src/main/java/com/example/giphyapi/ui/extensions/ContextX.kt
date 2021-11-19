package com.example.giphyapi.ui.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.giphyapi.models.Gif

/**
 * Using implicit intent to share the gif.
 */
fun Context.shareGif(gif: Gif) {
    val image = Uri.parse(gif.images?.original?.url)
    val streamType = "image/jpeg"

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, image)
        type = streamType
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    //Displaying all the possible applications that support sharing
    Intent.createChooser(sendIntent, null).also {
        startActivity(it)
    }
}