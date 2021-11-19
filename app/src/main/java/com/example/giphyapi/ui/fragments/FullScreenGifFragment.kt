package com.example.giphyapi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.giphyapi.R
import com.example.giphyapi.models.Gif
import com.example.giphyapi.ui.extensions.displaySnackbar
import com.example.giphyapi.ui.extensions.shareGif
import com.example.giphyapi.ui.extensions.showInfo
import kotlinx.android.synthetic.main.fragment_full_screen_gif.*


class FullScreenGifFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_full_screen_gif
    override val logTag = "FullScreenGifFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            //Fetching the data sent from the previous fragment
            FullScreenGifFragmentArgs.fromBundle(it).gif?.also { gif ->
                //Setting the menu
                activity?.startActionMode(actionModeCallback).also { actionMode -> actionMode?.tag = gif }
                //Displaying the gif
                Glide.with(this)
                    .asGif()
                    .load(gif.images?.original?.url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(fragment_full_screen_gif_iv_image)
            }
        }
    }

    //Creating the Contextual Action Mode - will be called after a long click on a gif.
    private val actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {

        //When clicking on the back button - go back to the previous fragment.
        override fun onDestroyActionMode(mode: ActionMode?) {
            view?.findNavController()?.popBackStack()
        }
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            //Inflating the new menu
            mode?.menuInflater?.inflate(R.menu.menu_contextual_action_mode, menu)
            return true
        }

        //finish() closes the contextual action mode.
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = when(item?.itemId) {

            R.id.toolbar_share -> {
                Log.d(logTag, "CAB - share clicked")
                context?.shareGif(mode?.tag as Gif)
                false
            }
            R.id.toolbar_save -> {
                Log.d(logTag, "CAB - save clicked")
                saveGif(mode?.tag as Gif)
                false
            }
            R.id.toolbar_info -> {
                Log.d(logTag, "CAB - info clicked")
                view?.showInfo(mode?.tag as Gif)
                false
            }
            else -> { true }
        }
    }

    //Didn't implement this function.
    fun saveGif(gif: Gif) {
        //TODO: implement the function
        view?.displaySnackbar(resources.getString(R.string.gif_saved_successfully))
    }
}