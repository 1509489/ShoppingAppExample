package com.pixelart.shoppingappexample.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

object Utils {

    fun getNumberOfColumns(context: Context): Int{
        val displayMetrics = context.resources.displayMetrics
        val displayWidth = displayMetrics.widthPixels / displayMetrics.density

        return (displayWidth / 180).toInt()
    }

    class GridItemDecorator(private val spacing: Int, private val spanCount: Int, private val edgeSpacing: Boolean): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)


            val position = parent.getChildAdapterPosition(view)
            val column: Int = position % spanCount


            if (position >= 0) {

                if (edgeSpacing) {
                    outRect.left = (spacing - column) * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing
                    }
                    outRect.bottom = spacing // item bottom
                } else {
                    outRect.left = column * (spacing / spanCount) // column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacing // item top
                    }
                }
            } else {
                outRect.left = 0
                outRect.right = 0
                outRect.top = 0
                outRect.bottom = 0
            }
        }

    }

}