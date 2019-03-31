package com.pixelart.shoppingappexample.model

data class OrdersResponse(
    val orders: List<Order>,
    val error: Boolean,
    val message: String
)