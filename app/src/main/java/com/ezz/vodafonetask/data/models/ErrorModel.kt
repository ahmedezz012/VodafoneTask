package com.ezz.vodafonetask.data.models

import androidx.annotation.DrawableRes
import com.ezz.vodafonetask.R

sealed class ErrorModel(
    val errorTitle: StringModel? = null,
    @DrawableRes val errorIcon: Int? = null
) {

    class NoDataError(errorTitle: StringModel? = StringModel(R.string.no_results_found)) :
        ErrorModel(errorTitle, R.drawable.ic_no_items)

    class NoNetworkError(
        errorTitle: StringModel? = StringModel(R.string.offline)
    ) : ErrorModel(errorTitle, R.drawable.ic_offline)

    class Error(
        errorTitle: StringModel? = StringModel(R.string.something_went_wrong)
    ) : ErrorModel(errorTitle, R.drawable.ic_error)


}
