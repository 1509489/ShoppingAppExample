package com.pixelart.shoppingappexample.common

import android.content.Context

object Utils {

    fun getNumberOfColumns(context: Context): Int{
        val displayMetrics = context.resources.displayMetrics
        val displayWidth = displayMetrics.widthPixels / displayMetrics.density

        return (displayWidth / 180).toInt()
    }
}