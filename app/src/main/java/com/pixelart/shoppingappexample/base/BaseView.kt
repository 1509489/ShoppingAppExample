package com.pixelart.shoppingappexample.base

import android.widget.ProgressBar

interface BaseView {
    fun showMessage(message: String)
    fun showError(error: String)
    fun showLoadingIndicator(loadingIndicator: ProgressBar)
    fun hideLoadingIndicator(loadingIndicator: ProgressBar)
    fun showProgress()
    fun hideProgress()
}