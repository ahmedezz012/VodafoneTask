package com.ezz.vodafonetask.data.models.view_state

import android.os.Parcelable
import android.text.TextUtils.TruncateAt
import android.widget.TextView
import kotlinx.parcelize.Parcelize

@Parcelize
internal class TextViewState(
    val text: CharSequence,
    val ellipsize: TruncateAt,
    val maxLines: Int
) : Parcelable {

    fun restoreState(textView: TextView) {
        textView.text = text
        textView.ellipsize = ellipsize
        textView.maxLines = maxLines
    }
}
