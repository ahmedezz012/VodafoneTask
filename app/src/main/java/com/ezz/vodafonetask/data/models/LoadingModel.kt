package com.ezz.vodafonetask.data.models

import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

data class LoadingModel(
        val shouldShow: Boolean,
        val progressType: ProgressTypes,
        var loadingProgressView: ProgressBar? = null,
        var pagingProgressView: ProgressBar? = null,
        var pullToRefreshProgressView: SwipeRefreshLayout? = null,
)
