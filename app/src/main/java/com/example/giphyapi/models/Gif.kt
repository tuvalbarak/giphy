package com.example.giphyapi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


//Using interface for scalability
interface IGif {
    val id: String?
    val title: String?
    val images: Image?
    val import_datetime: String?
}

//Parcelize lets me send a Gif between fragments (using SafeArgs)
@Parcelize
data class Gif (
    override val id: String?,
    override val title: String?,
    override val images: @RawValue Image?,
    override val import_datetime: String?,
    var isSelected: Boolean
) : IGif, Parcelable

data class Image (
    val original: Original?,
)

data class Original (
    val url: String?,
    val mp4: String?
)

//Creating a Gif wrapper class so I will be able to use it in Retrofit
data class GifResponse (
    val data: List<Gif>
)