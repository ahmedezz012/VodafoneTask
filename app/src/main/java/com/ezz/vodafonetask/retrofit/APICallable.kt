package com.ezz.vodafonetask.retrofit

interface APICallable<T> {

    fun call(timestampedTag: String): T?
}
