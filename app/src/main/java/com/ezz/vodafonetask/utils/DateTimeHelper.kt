package com.ezz.vodafonetask.utils

import java.util.concurrent.TimeUnit

object DateTimeHelper {

    fun getCurrentTimeStamp(): Long {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    }
}
