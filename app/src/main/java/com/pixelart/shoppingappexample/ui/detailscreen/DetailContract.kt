package com.pixelart.shoppingappexample.ui.detailscreen

import com.pixelart.shoppingappexample.base.BasePresenter
import com.pixelart.shoppingappexample.base.BaseView

interface DetailContract {
    interface View: BaseView{

    }
    interface Presenter: BasePresenter{
        fun addToCart( name: String, description: String, quantity: String, price: String,
                       imgUrl: String, customerId: String, productId: String)
    }
}