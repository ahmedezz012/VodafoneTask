package com.ezz.vodafonetask.ui.base

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ezz.vodafonetask.R
import com.ezz.vodafonetask.data.models.ErrorModel

interface IErrorViews {

    fun onError(
        context: Context,
        errorView: View,
        errorModel: ErrorModel,
        viewToHide: View? = null,
        onRetryClick: View.OnClickListener? = null
    ) {
        val txtErrorTitle = errorView.findViewById<TextView>(R.id.txtErrorTitle)
        val btnError = errorView.findViewById<Button>(R.id.btnError)
        val imgError = errorView.findViewById<ImageView>(R.id.imgError)

        if (viewToHide != null) viewToHide.visibility = View.GONE
        errorView.visibility = View.VISIBLE

        val errorTitle =
            errorModel.errorTitle?.getString(context)
        val errorIcon = errorModel.errorIcon

        if (!errorTitle.isNullOrBlank()) {
            txtErrorTitle.text = errorTitle
            txtErrorTitle.visibility = View.VISIBLE
        } else txtErrorTitle.visibility = View.GONE

        if (onRetryClick != null) {
            btnError.visibility = View.VISIBLE
            btnError.setOnClickListener(onRetryClick)
        } else btnError.visibility = View.GONE

        if (errorIcon != null) {
            imgError.visibility = View.VISIBLE
            imgError.setBackgroundResource(errorIcon)
        } else imgError.visibility = View.GONE
    }

    fun onSuccess(viewToShow: View? = null, errorView: View) {
        if (viewToShow != null) viewToShow.visibility = View.VISIBLE
        errorView.visibility = View.GONE
    }

    fun shouldShowErrorLayout(errorView: View, shouldShow: Boolean) {
        errorView.visibility = if (shouldShow) View.VISIBLE else View.GONE
    }
}
