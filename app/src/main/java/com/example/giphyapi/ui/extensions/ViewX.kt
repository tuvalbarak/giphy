package com.example.giphyapi.ui.extensions

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity
import com.example.giphyapi.models.Gif
import com.google.android.material.snackbar.Snackbar

/**
 * View extensions functions
 */

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

//Displaying a Snackbar.
fun View.displaySnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}


//Displaying a Dialog. If the buttons' text is an empty string - it won't be displayed.
fun View.displayDialog(title: String, message: String, positiveButtonText: String, negativeButtonText: String) {
    AlertDialog.Builder(context).apply {
        setTitle(title)
        setMessage(message)
        if(positiveButtonText.isNotBlank()) {
            setPositiveButton(positiveButtonText) { _, _ ->}
        }

        if(negativeButtonText.isNotBlank()) {
            setNegativeButton(negativeButtonText) { _, _ ->}
        }
    }.create().show()
}

fun View.showInfo(gif: Gif) {
    this.displayDialog(gif.title ?: "N/A", "Date: ${gif.import_datetime}", "OK", "")
}

//editText to String
var EditText.value
    get() = this.text.toString()
    set(value) { this.setText(value) }