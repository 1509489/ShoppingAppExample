package com.pixelart.shoppingappexample.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

            etQuantity.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mListener.onQuantityChange(position, s)
                }
            })
        }
    }

    inner class ViewHolder(val mView: View, val context: Context) : RecyclerView.ViewHolder(mView) {
        private val description: TextView = mView.tvDescription
        private val price: TextView = mView.tvPrice
        private val productImage: ImageView = mView.ivProductImage
        val etQuantity: EditText = mView.etQuantity
        val btnDelete: ImageButton = mView.ibDelete
        private val ibQtyIncrease: ImageButton = mView.ibQtyIncrease
        private val ibQtyDecrease: ImageButton = mView.ibQtyDecrease

        @SuppressLint("SetTextI18n")
        fun setContent(cartItem: CartItem){
            var quantity = cartItem.quantity.toInt()

            description.text = cartItem.description
            price.text = cartItem.price

            etQuantity.setText("$quantity")

            ibQtyIncrease.setOnClickListener {
                quantity += 1
                etQuantity.setText("$quantity")
            }

            ibQtyDecrease.setOnClickListener {
                if (quantity > 1)
                {
                    quantity -= 1
                    etQuantity.setText("$quantity")
                }
            }

            GlideApp.with(context)
                .load(cartItem.imageUrl)
                .into(productImage)

        }
    }

    interface OnItemClickedListener{
        fun onItemClicked(position: Int)
        fun onDeleteCartItem(position: Int)
        fun onQuantityChange(position: Int, quantity: CharSequence?)
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
