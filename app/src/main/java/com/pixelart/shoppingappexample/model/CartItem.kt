package com.pixelart.shoppingappexample.model

import com.google.gson.annotations.SerializedName

data class CartItem(
    val error: Boolean,
    val message: String,
    val id: String,
    val name: String,
    val description: String,
    val quantity: String,
    val price: String,
    @SerializedName("img_url") val imageUrl: String,
    @SerializedName("customer") val customerId: String,
    @SerializedName("product_id") val productId: String
)