package com.ezz.vodafonetask.data.models.dto

import com.ezz.vodafonetask.utils.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class APIResponse<T>(
    var result: T? = null,
    var status: Int = Constants.ApiStatus.STATUS_SUCCESS,
    var httpCode: Int = Constants.ApiStatus.NO_HTTP_CODE
) {

}