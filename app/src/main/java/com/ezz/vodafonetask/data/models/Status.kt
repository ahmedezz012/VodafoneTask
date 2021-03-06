package com.ezz.vodafonetask.data.models

sealed class Status<T>(
    val statusCode: StatusCode = StatusCode.IDLE,
    val data: T? = null,
    val errorModel: ErrorModel? = null,
    val errorCode: Int? = null
) {

    class Success<T>(data: T? = null, errorModel: ErrorModel? = null) :
        Status<T>(StatusCode.SUCCESS, data, errorModel)

    class Error<T>(data: T? = null, errorModel: ErrorModel? = null) :
        Status<T>(StatusCode.ERROR, data, errorModel)


    class NoData<T>(data: T? = null, errorModel: ErrorModel? = null) :
        Status<T>(StatusCode.NO_DATA, data, errorModel)

    class NoNetwork<T>(data: T? = null, errorModel: ErrorModel? = null) :
        Status<T>(StatusCode.NO_NETWORK, data, errorModel)

    class Idle<T>(data: T? = null, errorModel: ErrorModel? = null) :
        Status<T>(StatusCode.IDLE, data, errorModel)

    class OfflineData<T>(data: T? = null, errorModel: ErrorModel? = null) :
        Status<T>(StatusCode.OFFLINE_DATA, data, errorModel)

    class CopyStatus<T, R>(status: Status<T>, newData: R?) :
        Status<R>(status.statusCode, newData, status.errorModel, status.errorCode)

    // Checkers
    fun isSuccess(): Boolean {
        return statusCode == StatusCode.SUCCESS
    }

    fun isError(): Boolean {
        return statusCode == StatusCode.ERROR
    }

    fun isOfflineData(): Boolean {
        return statusCode == StatusCode.OFFLINE_DATA
    }

    fun isNoNetwork(): Boolean {
        return statusCode == StatusCode.NO_NETWORK
    }

    fun isNoData(): Boolean {
        return statusCode == StatusCode.NO_DATA
    }

    fun isValid(): Boolean {
        return statusCode == StatusCode.VALID
    }

    fun isIdle(): Boolean {
        return statusCode == StatusCode.IDLE
    }
}
