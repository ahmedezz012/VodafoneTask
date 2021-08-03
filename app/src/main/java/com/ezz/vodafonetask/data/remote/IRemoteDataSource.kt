package com.ezz.vodafonetask.data.remote

import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.data.models.dto.APIResponse


interface IRemoteDataSource {

    fun cancelRequest(tag: String)

    fun getPhotosList(
        pageIndex: Int,
        pageSize: Int,
        tag: String
    ): APIResponse<ArrayList<PhotoItem>?>
}
