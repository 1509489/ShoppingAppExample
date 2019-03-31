package com.pixelart.shoppingappexample.model

data class OrderNumberResponse(
    val orderNumber: String,
    val error: Boolean,
    val message: String
)