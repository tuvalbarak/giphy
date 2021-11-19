package com.example.giphyapi.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.giphyapi.R
import com.example.giphyapi.models.Gif
import com.example.giphyapi.ui.adapters.GifAdapter
import com.example.giphyapi.ui.extensions.showInfo
import com.example.giphyapi.ui.extensions.shareGif
import com.example.giphyapi.ui.extensions.displayDialog
import com.example.giphyapi.ui.extensions.displaySnackbar
import com.example.giphyapi.ui.extensions.show
import com.example.giphyapi.ui.extensions.gone
import com.example.giphyapi.utils.States
import com.example.giphyapi.viewmodels.GifViewModel
import com.example.giphyapi.viewmodels.ViewModelFactory
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.fragment_gifs.*
import java.util.concurrent.TimeUnit


class GifFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_gifs
    override val logTag = "GifFragment"
    private var music: MediaPlayer? = null

    //Lazy initialization of VM
    private val gifViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.create(requireContext())).get(GifViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Asking for permissions.
        activityResultLauncher.launch(
            arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
        setupRecyclerView()
        setupState()
        setupGifList()
    }

    /**
     * onResume and onPause are used with no choice - I have to stop/play the music according the fragment's lifecycle.
     */
    override fun onResume() {
        //Getting shared preferences reference
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        //Fetching the music last saved state
        val volume = sharedPref?.getBoolean(resources.getString(R.string.preference_volume_key), true) ?: true
        if(volume) {
            playBackgroundMusic()
        }
        super.onResume()
    }

    override fun onPause() {
        stopBackgroundMusic()
        super.onPause()
    }

    /**
     * Generating a random number so every time the user opens that app - a different song will be streamed.
     */
    private fun playBackgroundMusic() {
        Log.d(logTag, "playBackgroundMusic")
        val numberOfSongs = 3
        val song = when ((1 .. numberOfSongs).random()) {
            1 -> R.raw.background_music_1
            2 -> R.raw.background_music_2
            else -> R.raw.background_music_3
        }
        music = MediaPlayer.create(requireContext(), song).apply { start() }
    }

    /**
     * Stops the current streamed music (if there's no music - do nothing).
     */
    private fun stopBackgroundMusic() {
        Log.d(logTag, "stopBackgroundMusic")
        music?.apply {
            if(isPlaying) {
                stop()
            }
        }
    }

    private fun setupRecyclerView() {

        //Lambda function that will invoked after a long click on a row in the recyclerview -> replacing the current toolbar with Contextual Action Mode,
        // providing the user with actions on the selected gif.
        val onGifLongClicked: (gif: Gif) -> Unit = { gif ->
            activity?.startActionMode(actionModeCallback).also { it?.tag = gif }
        }

        //Lambda function that will invoked after a click on a row in the recyclerview -> Navigating to FullScreenGifFragment (which will display
        // the gif), while passing it the gif using SafeArgs (and Parcelize in the Model).
        val onGifClicked: (gif: Gif) -> Unit = { gif ->
            view?.findNavController()?.navigate(
                    GifFragmentDirections.navActionFullScreenGifFragment(gif)
            )
        }

        //Binding the recyclerview and the adapter.
        fragment_gifs_rv_recyclerview.adapter = GifAdapter(onGifClicked, onGifLongClicked)
    }

    /**
     * Observing the current app state.
     * Using View extension functions (cleaner and easier to read) to change the visibility of the progress bar.
     */
    private fun setupState() {
        gifViewModel.state.observe(viewLifecycleOwner, Observer { state ->

            when (state) {
                States.Idle -> {
                    Log.d(logTag, "Idle")
                    fragment_gifs_pb_progress_bar.gone()
                }
                States.Loading -> {
                    Log.d(logTag, "Loading")
                    fragment_gifs_pb_progress_bar.show()
                }
                else -> return@Observer
            }
        })
    }

    /**
     * Observing GifsList, which holds the current list that needs to be displayed.
     * Using submitList to achieve better performance while changes occur.
     */
    private fun setupGifList() {
        gifViewModel.gifList.observe(viewLifecycleOwner, Observer { gifs ->
            (fragment_gifs_rv_recyclerview.adapter as GifAdapter).submitList(gifs)
        })
    }

    // Overriding the onCreateOptionsMenu -> taking care of the searchView and the stop/play button.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //Inflating the menu (toolbar)
        inflater.inflate(R.menu.menu_toolbar, menu)
        startAndStopBackgroundMusic(menu)
        searchViewObserver(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Listening for changes in the search input box - notifying the VM about changes.
    @SuppressLint("CheckResult")
    private fun searchViewObserver(menu: Menu) {
        //If the user search for a text, clicks on a gif, and then returns to the first fragment ->
        // resetting the query (if I used dialog then it would be easier to keep his last search.
        gifViewModel.onQueryChanged("")

        val searchView = menu.findItem(R.id.toolbar_search).actionView as SearchView

        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        })  .debounce(400, TimeUnit.MILLISECONDS) //Waiting 0.4 seconds without typing
                .subscribe { text -> gifViewModel.onQueryChanged(text) } //Query the new text
    }

   // Toggling the state of background music according to the user's choice (saving the state to Shared Preferences).
    private fun startAndStopBackgroundMusic(menu: Menu) {

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        var isPlaying = (sharedPref?.getBoolean(resources.getString(R.string.preference_volume_key), true) ?: false)

        if(!isPlaying) { //If music isn't being streamed - change the icon (only for the first time, rest of the toggles will happen in the click listener below.
            menu.findItem(R.id.toolbar_volume).icon = ResourcesCompat.getDrawable(resources ,R.drawable.ic_baseline_volume_off, null)
        }

        //Toggling the state of the volume button.
        menu.findItem(R.id.toolbar_volume).setOnMenuItemClickListener { volumeIcon ->

            isPlaying = (sharedPref?.getBoolean(resources.getString(R.string.preference_volume_key), true) ?: false)

            when(isPlaying) {
                false -> {
                    volumeIcon.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_volume_up, null)
                    //If it's the first time the music needs to be played -> initialize it through playBackgroundMusic. Otherwise -> start streaming.
                    music?.start() ?: run { playBackgroundMusic() }
                }
                true -> {
                    volumeIcon.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_volume_off, null)
                    music?.pause() //Pausing the music
                }
            }
            //Toggling isPlaying and inserting the new value into shared preferences.
            isPlaying = !isPlaying
            sharedPref?.edit()
                    ?.putBoolean(resources.getString(R.string.preference_volume_key), isPlaying)
                    ?.apply()
            true
        }
    }

    //Creating the Contextual Action Mode - will be called after a long click on a gif.
    private val actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {

        override fun onDestroyActionMode(mode: ActionMode?) {}
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_contextual_action_mode, menu) //Inflating the new menu
            return true
        }

        //finish() closes the contextual action mode.
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = when(item?.itemId) {

            R.id.toolbar_share -> {
                Log.d(logTag, "CAB - share clicked")
                mode?.finish()
                context?.shareGif(mode?.tag as Gif)
                true
            }
            R.id.toolbar_save -> {
                Log.d(logTag, "CAB - save clicked")
                mode?.finish()
                saveGif(mode?.tag as Gif)
                true
            }
            R.id.toolbar_info -> {
                Log.d(logTag, "CAB - info clicked")
                mode?.finish()
                view?.showInfo(mode?.tag as Gif)
                true
            }
            else -> { true }
        }
    }

    //Didn't implement this function.
    fun saveGif(gif: Gif) {
        //TODO: implement the function
        view?.displaySnackbar(resources.getString(R.string.gif_saved_successfully))
    }

    /**
     * Asking for permissions in runtime.
     */
    private var activityResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->

            var allAreGranted = true
            //Looking for a denied permission.
            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }
            //If one or more of the permissions are denied - show a dialog with information regarding the permission use.
            if(!allAreGranted) {
                requireView().displayDialog(
                        resources.getString(R.string.permission_denied_dialog_title),
                        resources.getString(R.string.permission_denied_dialog_message),
                        resources.getString(R.string.permission_denied_dialog_positive_btn_text),
                        resources.getString(R.string.permission_denied_dialog_negative_btn_text)
                )
            }
        }

}