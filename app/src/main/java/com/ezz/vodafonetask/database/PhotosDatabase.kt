package com.ezz.vodafonetask.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.utils.Constants.Database.DB_VERSION

@Database(
    entities = [
        PhotoItem::class
    ],
    version = DB_VERSION
)
abstract class PhotosDatabase : RoomDatabase() {
    abstract fun getPhotosDao(): PhotosDao
}