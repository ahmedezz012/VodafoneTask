package com.ezz.vodafonetask.data.models

import android.graphics.Bitmap
import com.ezz.vodafonetask.data.models.dto.PhotoItem

data class PhotosListItem(
    val listItemType: ListItemType = ListItemType.PHOTO_ITEM,
    val photoItem: PhotoItem?,
    var bitmap: Bitmap? = null
)
