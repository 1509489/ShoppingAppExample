package com.pixelart.shoppingappexample.model

data class OrderDetail(
    val id: String,
    val orderNumber: String,
    val name: String,
    val description: String,
    val quantity: String,
    val price: String,
    val imgUrl: String
    )