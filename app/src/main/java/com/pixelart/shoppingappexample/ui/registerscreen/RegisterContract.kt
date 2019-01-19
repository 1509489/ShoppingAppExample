package com.pixelart.shoppingappexample.ui.registerscreen

import com.pixelart.shoppingappexample.model.Customer

interface RegisterContract {
    interface View{
        fun showHideLoadingIndicator(isLoading: Boolean)
        fun onRegisterSuccess(customer: Customer)
        fun showMessage(message: String)
        fun showError(error: String)
    }
    interface Presenter{
        fun initiateRegister(firstName:String, lastName:String, phoneNumber:String,
                             email:String, userName:String, password:String)
    }
}