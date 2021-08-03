package com.ezz.vodafonetask.ui.base

import androidx.annotation.CallSuper
import com.ezz.vodafonetask.data.models.Status
import com.ezz.vodafonetask.data.models.dto.APIResponse
import com.ezz.vodafonetask.data.remote.IRemoteDataSource
import com.ezz.vodafonetask.retrofit.APICallable
import com.ezz.vodafonetask.retrofit.RetrofitConfigurations
import com.ezz.vodafonetask.utils.ConnectionUtils
import com.ezz.vodafonetask.utils.Constants
import com.ezz.vodafonetask.utils.DateTimeHelper
import io.reactivex.Single
import java.util.concurrent.Callable

abstract class BaseRepository(
    private val connectionUtils: ConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource
) : IBaseRepository {

    private val mTagsList = ArrayList<String>()

    protected val isConnected: Boolean
        get() {
            return connectionUtils.isConnected
        }

    protected fun <S> createSingle(callable: Callable<S>): Single<S> {
        return Single.fromCallable { callable.call() }
    }

    /**
     * Calls the API without validating the response for unauthorized access.
     *
     * And should be used in the APIs that does not have token or authorization checks like login API.
     *
     * @param tag The API tag.
     * @param apiCallable the APICallable for the API.
     */
    protected fun <T> callAPI(
        tag: String,
        apiCallable: APICallable<T>
    ): Single<Status<T>> {
        val timestampedTag = getTimestampedTag(tag)
        addTag(timestampedTag)

        return createSingle(Callable<Status<T>> {
            if (isConnected) {
                val response = apiCallable.call(timestampedTag)
                Status.Success(response)
            } else {
                Status.NoNetwork()
            }
        })
            .map { status ->
                return@map onAPIResponse(timestampedTag, status)
            }
    }

    private fun <T> onAPIResponse(tag: String, status: Status<T>): Status<T> {
        removeTag(tag)

        // If cancel code return Idle()
        return if (status.data is APIResponse<*>? &&
            status.data?.httpCode == RetrofitConfigurations.CANCELED_HTTP_CODE
        )
            Status.Idle()
        else
            status
    }

    /**
     * @param extraHeaders this method can take extra headers to add it to the predefined app's headers
     */
    protected fun getHeaders(extraHeaders: Map<String, String> = emptyMap()): Map<String, String> {
        val headers = HashMap<String, String>()
        headers.putAll(extraHeaders)

        headers[Constants.Network.CONTENT_TYPE] = Constants.Network.APPLICATION_JSON
        return headers
    }

    /**
     * The Issue:
     *
     * To solve the problem of the same api request from multiple screens, When a view wants to cancel
     * It's request of a specific api It would cancel other Screen's request also as they have the same tag.
     *
     * The Solution:
     *
     * So we add a timestamp to the tag before request to differentiate between different requests of the same api.
     */
    private fun getTimestampedTag(tag: String): String {
        return tag + DateTimeHelper.getCurrentTimeStamp()
    }

    private fun removeTag(tag: String) {
        mTagsList.remove(tag)
    }

    private fun addTag(tag: String) {
        if (!mTagsList.contains(tag))
            mTagsList.add(tag)
    }

    override fun cancelAPI(tagPrefix: String) {
        val filteredList = mTagsList.filter { it.startsWith(tagPrefix) }
        for (tag in filteredList) {
            removeTag(tag)
            mIRemoteDataSource.cancelRequest(tag)
        }
    }

    @CallSuper
    override fun cancelAPIs() {
        for (tag in mTagsList)
            mIRemoteDataSource.cancelRequest(tag)
    }
}
