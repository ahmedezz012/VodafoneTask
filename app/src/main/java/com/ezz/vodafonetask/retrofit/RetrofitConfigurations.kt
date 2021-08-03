package com.ezz.vodafonetask.retrofit

import com.ezz.vodafonetask.data.models.dto.APIResponse
import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.utils.Constants
import com.ezz.vodafonetask.utils.Utils
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


open class RetrofitConfigurations protected constructor() {

    private val mCalls = HashMap<String, Call<*>>()

    companion object {
        const val CANCELED_HTTP_CODE = 411
    }

    @Throws(Exception::class)
    protected fun <R> executeAPIResponseCall(call: Call<*>, tag: String): APIResponse<R> {
        val response = executeApiCall<R>(call, tag)
        return getApiResponse(response)
    }

    @Throws(Exception::class)
    protected fun <R> executeApiCall(call: Call<*>, tag: String): Response<R> {
        return try {
            mCalls[tag] = call
            val response: Response<R> = call.execute() as Response<R>
            mCalls.remove(tag)
            response
        } catch (e: IOException) {
            Utils.printStackTrace(e)
            if (call.isCanceled) {
                Response.error(CANCELED_HTTP_CODE, "".toResponseBody(null))
            } else
                throw (e)
        }
    }

    fun cancelRetrofitRequest(tag: String?) {
        val call = mCalls[tag]
        if (call != null) {
            call.cancel()
            mCalls.remove(tag)
        }
    }

    private fun <R> getApiResponse(response: Response<R>): APIResponse<R> {
        var apiResponse: APIResponse<R> = APIResponse()
        if (response.isSuccessful && response.body() != null) {
            apiResponse.result = response.body()!!
            return apiResponse
        } else {
            apiResponse = getErrorBody(response)
        }
        apiResponse.httpCode = response.code()
        return apiResponse
    }

    private fun <R> getErrorBody(response: Response<R>): APIResponse<R> {
        return try {
            val gson = GsonBuilder().create()
            val responseString = response.errorBody()?.string()
            if (!responseString.isNullOrBlank())
                gson.fromJson<APIResponse<R>>(responseString, APIResponse::class.java)
            else
                APIResponse(status = Constants.ApiStatus.STATUS_FAIL)
        } catch (e: java.lang.Exception) {
            Utils.printStackTrace(e)
            APIResponse(status = Constants.ApiStatus.STATUS_FAIL)
        }
    }
}
