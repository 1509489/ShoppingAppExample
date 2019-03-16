package com.pixelart.shoppingappexample.ui.detailscreen

import com.pixelart.shoppingappexample.base.BasePresenter
import com.pixelart.shoppingappexample.base.BaseView
import com.pixelart.shoppingappexample.model.Product

interface DetailContract {
    interface View: BaseView{
        fun showDetails(product: Product)
    }
    interface Presenter: BasePresenter{
        fun getDetails()
        fun addToCart( name: String, description: String, quantity: String, price: String,
                       imgUrl: String, customerId: String, productId: String)
    }
}