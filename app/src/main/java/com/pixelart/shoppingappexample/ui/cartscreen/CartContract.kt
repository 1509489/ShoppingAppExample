package com.pixelart.shoppingappexample.ui.cartscreen

import com.pixelart.shoppingappexample.base.BasePresenter
import com.pixelart.shoppingappexample.base.BaseView
import com.pixelart.shoppingappexample.model.CartResponse
import com.pixelart.shoppingappexample.model.OrderNumberResponse

interface CartContract {
    interface View : BaseView {
        fun showCartItem(cartResponse: CartResponse?)
        fun getOrderNumber(orderResponse: OrderNumberResponse)
        fun getBraintreeToken(token: String)
    }

    interface Presenter : BasePresenter {
        fun getCartItems(customerId: String)
        fun deleteCartItem(itemId: String, customerId: String)
        fun setQuantity(quantity: String, customerId: String, productId: String)
        fun addOrder(customerId: String, totalPrice: String, totalItems: String)
        fun addOrderDetails(orderNumber: String, name: String, description: String,
                            quantity: String, totalPrice: String, imgUrl: String)
        fun getBraintreeToken()
        fun checkout(nounce: String, amount: String)
    }
}