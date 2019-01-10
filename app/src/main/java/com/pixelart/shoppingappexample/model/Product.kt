package com.pixelart.shoppingappexample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String
):Parcelable