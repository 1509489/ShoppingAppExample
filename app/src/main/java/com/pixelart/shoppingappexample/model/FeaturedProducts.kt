package com.pixelart.shoppingappexample.model

data class FeaturedProducts(
    val products: List<Product>,
    val error: Boolean,
    val message: String
)