package com.ezz.vodafonetask.data.local

import android.content.Context
import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.database.PhotosDatabase
import com.ezz.vodafonetask.utils.Utils

class LocalDataSource(private val mContext: Context, private val photosDatabase: PhotosDatabase) :
    ILocalDataSource {

    override fun insertPhotos(photosList: ArrayList<PhotoItem>): List<Long> {
        return photosDatabase.getPhotosDao().insertPhotos(photosList)
    }

    override fun deleteAllPhotos(): Int {
        return photosDatabase.getPhotosDao().deleteAll()
    }

    override fun getAllPhotos(): ArrayList<PhotoItem> {
        return Utils.convertListToArrayList(photosDatabase.getPhotosDao().getAllPhotos())
    }
}
