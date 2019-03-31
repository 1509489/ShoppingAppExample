package com.pixelart.shoppingappexample.model

data class Order(
    val orderNumber: String,
    val customerId: String,
    val totalPrice: String,
    val totalItems: String
)