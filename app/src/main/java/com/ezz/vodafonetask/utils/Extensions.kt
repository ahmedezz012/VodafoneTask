package com.ezz.vodafonetask.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ezz.vodafonetask.R
import java.util.*

fun Any?.getString(context: Context, locale: Locale, vararg args: Any?): String? {
    return when (this) {
        is Int -> String.format(locale, context.getString(this), *args)
        is String -> String.format(locale, this, *args)
        else -> null
    }
}

fun AppCompatImageView.loadImageUrl(
    url: String?
) {
    Glide.with(this.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.place_holder)
        .error(R.drawable.place_holder)
        .centerCrop().into(this)
}

fun String?.alternate(alternate: String): String {
    return if (this.isNullOrEmpty()) alternate
    else this
}

