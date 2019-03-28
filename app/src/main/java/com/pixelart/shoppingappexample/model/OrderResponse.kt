package com.pixelart.shoppingappexample.model

data class OrderResponse(
    val orderNumber: String,
    val error: Boolean,
    val message: String
)