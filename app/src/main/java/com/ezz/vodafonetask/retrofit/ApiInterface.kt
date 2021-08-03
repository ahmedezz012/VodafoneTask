package com.ezz.vodafonetask.retrofit

import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET(Constants.URL.PHOTOS_LISTING)
    fun getPhotosList(
        @Query(Constants.ApiParams.PAGE_INDEX) pageIndex: Int,
        @Query(Constants.ApiParams.PAGE_SIZE) pageSize: Int
    ): Call<ArrayList<PhotoItem>>

}
