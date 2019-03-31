package com.pixelart.shoppingappexample.ui.orderscreen

import com.pixelart.shoppingappexample.base.BasePresenter
import com.pixelart.shoppingappexample.base.BaseView
import com.pixelart.shoppingappexample.model.OrdersResponse

interface OrderContract {
    interface View: BaseView{
        fun showOrders(ordersResponse: OrdersResponse)
    }
    interface Presenter:BasePresenter{
        fun getOrders(customerId: String)
    }
}