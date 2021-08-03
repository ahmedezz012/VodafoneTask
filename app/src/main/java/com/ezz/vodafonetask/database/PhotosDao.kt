package com.ezz.vodafonetask.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ezz.vodafonetask.data.models.dto.PhotoItem

@Dao
interface PhotosDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPhotos(photosList: ArrayList<PhotoItem>): List<Long>

    @Query("SELECT * FROM Photos")
    fun getAllPhotos(): List<PhotoItem>

    @Query("DELETE FROM Photos")
    fun deleteAll():Int
}
