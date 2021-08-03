package com.ezz.vodafonetask.ui.base

interface IBaseUseCase {

    fun cancelAPI(tagPrefix: String)

    fun cancelAPIs()

    fun onCleared()
}
