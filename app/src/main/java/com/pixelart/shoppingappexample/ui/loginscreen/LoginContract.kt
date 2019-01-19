package com.pixelart.shoppingappexample.ui.loginscreen

import com.pixelart.shoppingappexample.model.Customer

interface LoginContract {
    interface View{
        fun showHideLoadingIndicator(isLoading: Boolean)
        fun onLoginSuccess(customer: Customer)
        fun showMessage(message: String)
        fun showError(error: String)
    }

    interface Presenter{
        fun initiateLogIn(username: String, password: String)
    }
}