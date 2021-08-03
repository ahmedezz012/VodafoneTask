package com.ezz.vodafonetask.data.remote

import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.data.models.dto.APIResponse
import com.ezz.vodafonetask.retrofit.ApiInterface
import com.ezz.vodafonetask.retrofit.RetrofitConfigurations


class RemoteDataSource(private val mRetrofitInterface: ApiInterface) :
    RetrofitConfigurations(), IRemoteDataSource {

    override fun cancelRequest(tag: String) {
        cancelRetrofitRequest(tag)
    }

    override fun getPhotosList(
        pageIndex: Int,
        pageSize: Int,
        tag: String
    ): APIResponse<ArrayList<PhotoItem>?> {
        return executeAPIResponseCall(
            mRetrofitInterface.getPhotosList(pageIndex, pageSize),
            tag
        )
    }

}
