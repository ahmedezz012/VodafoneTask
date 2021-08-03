package com.ezz.vodafonetask.ui.base

import androidx.annotation.CallSuper
import com.ezz.vodafonetask.data.models.*

open class BaseUseCase(private val mBaseRepository: IBaseRepository) : IBaseUseCase {

    @CallSuper
    protected fun validateResponse(statusResponse: Status<*>?): StatusCode {
        if (statusResponse == null)
            return StatusCode.ERROR

        if (statusResponse.isOfflineData())
            return StatusCode.OFFLINE_DATA

        if (statusResponse.isIdle())
            return StatusCode.IDLE

        if (statusResponse.isNoNetwork())
            return StatusCode.NO_NETWORK

        if (statusResponse.isError())
            return StatusCode.ERROR

        if (statusResponse.data == null)
            return StatusCode.ERROR

        return StatusCode.VALID
    }


    override fun cancelAPI(tagPrefix: String) {
        mBaseRepository.cancelAPI(tagPrefix)
    }

    override fun cancelAPIs() {
        mBaseRepository.cancelAPIs()
    }

    override fun onCleared() {
        cancelAPIs()
    }
}
