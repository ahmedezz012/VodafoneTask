package com.ezz.vodafonetask.data.models

import android.content.Context
import com.ezz.vodafonetask.utils.getString
import java.util.*

class StringModel(private val target: Any?, private vararg val args: Any?) {

    fun getString(context: Context, locale: Locale = Locale.getDefault()): String? =
        target.getString(context, locale, *args)
}
