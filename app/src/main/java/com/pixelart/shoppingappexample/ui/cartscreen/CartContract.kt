package com.pixelart.shoppingappexample.ui.cartscreen

import com.pixelart.shoppingappexample.base.BasePresenter
import com.pixelart.shoppingappexample.base.BaseView
import com.pixelart.shoppingappexample.model.CartResponse

interface CartContract {
    interface View: BaseView{
        fun showCartItem(cartResponse: CartResponse?)
    }
    interface Presenter: BasePresenter{
        fun getCartItems(customerId: String)
        fun deleteCartItem(itemId: String, customerId: String)
    }
}