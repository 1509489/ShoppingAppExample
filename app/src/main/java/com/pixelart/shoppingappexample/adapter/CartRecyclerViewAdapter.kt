package com.pixelart.shoppingappexample.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.common.GlideApp
import com.pixelart.shoppingappexample.model.CartItem

import kotlinx.android.synthetic.main.cart_rv_layout.view.*

class CartRecyclerViewAdapter( private val mListener: OnItemClickedListener) :
    ListAdapter<CartItem, CartRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_rv_layout, parent, false)
        return ViewHolder(view,parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.apply {
            setContent(item)
            mView.setOnClickListener { mListener.onItemClicked(position) }
            btnDelete.setOnClickListener { mListener.onDeleteCartItem(position) }
        }
    }

    inner class ViewHolder(val mView: View, val context: Context) : RecyclerView.ViewHolder(mView) {
        private val description: TextView = mView.tvDescription
        private val price: TextView = mView.tvPrice
        private val productImage: ImageView = mView.ivProductImage
        private val quantity: Spinner = mView.spQuantity
        val btnDelete: ImageButton = mView.ibDelete

        fun setContent(cartItem: CartItem){
            val qty = cartItem.quantity
            val prc = cartItem.price
            val tPrc = prc.toDouble() * qty.toInt()

            description.text = cartItem.description
            price.text = "£$tPrc"
            //quantity.

            GlideApp.with(context)
                .load(cartItem.imageUrl)
                .into(productImage)
        }
    }

    interface OnItemClickedListener{
        fun onItemClicked(position: Int)
        fun onDeleteCartItem(position: Int)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<CartItem> = object : DiffUtil.ItemCallback<CartItem>(){
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
