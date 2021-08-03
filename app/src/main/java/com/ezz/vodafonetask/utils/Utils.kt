package com.ezz.vodafonetask.utils

import android.content.Context
import android.widget.Toast
import com.ezz.vodafonetask.BuildConfig


object Utils {
    fun printStackTrace(exception: Throwable) {
        if (BuildConfig.DEBUG) {
            exception.printStackTrace()
        }
    }

    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun <T> convertListToArrayList(list: List<T>?): ArrayList<T> {
        val arrayList = arrayListOf<T>()
        if (!list.isNullOrEmpty()) {
            list.toCollection(arrayList)
        }
        return arrayList
    }

}