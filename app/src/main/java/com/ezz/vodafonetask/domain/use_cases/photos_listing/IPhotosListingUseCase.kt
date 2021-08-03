package com.ezz.vodafonetask.domain.use_cases.photos_listing

import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.ui.base.IBaseUseCase
import com.ezz.vodafonetask.data.models.Status
import io.reactivex.Single

interface IPhotosListingUseCase : IBaseUseCase {
    fun getPhotosList(
        pageIndex: Int,
        pageSize: Int,
        tag: String
    ): Single<Status<ArrayList<PhotoItem>?>>
}