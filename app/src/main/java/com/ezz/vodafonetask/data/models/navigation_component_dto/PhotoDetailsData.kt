package com.ezz.vodafonetask.data.models.navigation_component_dto

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PhotoDetailsData(
    val id: String,
    val downloadUrl: String? = null,
    val dominantColor: Int
) : Parcelable