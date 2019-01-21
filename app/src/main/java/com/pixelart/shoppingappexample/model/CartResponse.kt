package com.pixelart.shoppingappexample.model

import com.google.gson.annotations.SerializedName

data class CartResponse(
    @SerializedName("cart_items")
    val cartItems: List<CartItem>
)