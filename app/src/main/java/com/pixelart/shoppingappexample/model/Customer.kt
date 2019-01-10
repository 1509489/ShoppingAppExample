package com.pixelart.shoppingappexample.model

data class Customer(
    var error: Boolean? = null,
    var message: String? = null,
    var id: Int? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var phonenumber: String? = null,
    var email: String? = null,
    var username: String? = null
)