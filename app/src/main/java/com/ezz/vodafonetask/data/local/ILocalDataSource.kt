package com.ezz.vodafonetask.data.local

import com.ezz.vodafonetask.data.models.dto.PhotoItem

interface ILocalDataSource {

    fun insertPhotos(photosList: ArrayList<PhotoItem>): List<Long>

    fun deleteAllPhotos(): Int

    fun getAllPhotos(): ArrayList<PhotoItem>
}
