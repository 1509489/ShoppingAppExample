package com.pixelart.shoppingappexample.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.model.Order

class OrdersAdapter(private val listener: OnItemClickedListener):ListAdapter<Order, OrdersAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var context: Context
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.orders_rv_layout, parent, false)
        return ViewHolder(view, context)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.apply {
            setContent(order)
            itemView.setOnClickListener { listener.onItemClicked(position) }
        }
    }
    
    class ViewHolder( view: View, private val context: Context): RecyclerView.ViewHolder(view){
        private val tvOrderNumber: TextView = view.findViewById(R.id.tvOrderNumber)
        private val tvTotalPrice: TextView = view.findViewById(R.id.tvTotalPrice)
        private val tvTotalItems: TextView = view.findViewById(R.id.tvTotalItems)
        
        @SuppressLint("SetTextI18n")
        fun setContent(order: Order){
            val s = context.resources.getString(R.string.items)
            tvOrderNumber.text = "#${order.orderNumber}"
            tvTotalItems.text = "$s(${order.totalItems})"
            tvTotalPrice.text = "£${order.totalPrice}"
        }
    }
    
    interface OnItemClickedListener{
        fun onItemClicked(position: Int)
    }
    
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Order> = object : DiffUtil.ItemCallback<Order>(){
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.orderNumber == newItem.orderNumber
            }
    
            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.orderNumber == newItem.orderNumber
            }
        }
    }
}