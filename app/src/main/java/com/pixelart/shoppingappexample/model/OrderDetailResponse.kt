package com.pixelart.shoppingappexample.model

data class OrderDetailResponse(
    val orderDetails: List<OrderDetail>,
    val error: Boolean,
    val message: String
)