package com.pixelart.shoppingappexample.base

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment<Presenter: BasePresenter>: Fragment(), BaseView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingIndicator(loadingIndicator: ProgressBar) {
        loadingIndicator.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator(loadingIndicator: ProgressBar) {
        loadingIndicator.visibility = View.INVISIBLE
    }

    abstract fun init()
    abstract fun getViewPresenter(): Presenter
}