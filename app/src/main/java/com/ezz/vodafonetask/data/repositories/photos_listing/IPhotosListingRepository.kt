package com.ezz.vodafonetask.data.repositories.photos_listing

import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.ui.base.IBaseRepository
import com.ezz.vodafonetask.data.models.Status
import com.ezz.vodafonetask.data.models.dto.APIResponse
import io.reactivex.Single

interface IPhotosListingRepository : IBaseRepository {
    fun getPhotosList(
        pageIndex: Int,
        pageSize: Int,
        tag: String
    ): Single<Status<APIResponse<ArrayList<PhotoItem>?>>>

    fun getOfflinePhotosList(): Single<ArrayList<PhotoItem>>

    fun deleteAllPhotos(): Single<Int>

    fun insertPhotos(photosList: ArrayList<PhotoItem>): Single<List<Long>>
}