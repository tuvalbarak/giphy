package com.example.giphyapi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.giphyapi.R
import com.example.giphyapi.models.Gif
import kotlinx.android.synthetic.main.holder_row_gif.view.*


/**
 * Using Diffutils to compare between two gifs.
 */
object GifDiffCallback : DiffUtil.ItemCallback<Gif>() {
    override fun areItemsTheSame(oldGif: Gif, newGif: Gif) = oldGif.id == newGif.id
    override fun areContentsTheSame(oldGif: Gif, newGif: Gif) = oldGif.id == newGif.id
}

/**
 * @property itemView - current item in the recyclerview.
 * @property onGifClickListener - lambda function for click handling.
 * @property onGifLongClickListener - lambda function for long click handling.
 * This class is responsible for binding the data for each row in the recyclerview.
 */
class GifViewHolder(
    itemView: View,
    private val onGifClickListener: (gif: Gif) -> Unit,
    private val onGifLongClickListener: (gif: Gif) -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bind(gif: Gif) {

        itemView.apply {

            //Using Glide to load and display the gif.
            Glide.with(this)
                .asGif()
                .load(gif.images?.original?.url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder_row_gif_iv_image)

            //Invoking the lambda functions provided by the UI.
            this.setOnLongClickListener {
//                val alphaOfSelectedItem = 0.7F
//                alpha = if(!gif.isSelected) alphaOfSelectedItem else 1F
                onGifLongClickListener.invoke(gif)
                true
            }

            this.setOnClickListener {
                onGifClickListener.invoke(gif)
            }
        }
    }
}

/**
 * @property onGifClickListener - lambda function for click handling.
 * @property onGifLongClickListener - lambda function for click handling.
 */
class GifAdapter(
    private val onGifClickListener: (gif: Gif) -> Unit,
    private val onGifLongClickListener: (gif: Gif) -> Unit) : ListAdapter<Gif, GifViewHolder>(GifDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GifViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.holder_row_gif, parent, false),
            onGifClickListener,
            onGifLongClickListener
        )

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}