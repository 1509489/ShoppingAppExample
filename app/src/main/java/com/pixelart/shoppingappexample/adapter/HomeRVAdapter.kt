package com.pixelart.shoppingappexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.common.GlideApp
import com.pixelart.shoppingappexample.model.Product

class HomeRVAdapter(private val listener: OnItemClickedListener): ListAdapter<Product, HomeRVAdapter.ViewHolder>(DIFF_CALLBACK) {


    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRVAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.home_rv_layout, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: HomeRVAdapter.ViewHolder, position: Int) {
        val product = getItem(position)
        holder.apply { setContent(product) }

        holder.itemView.setOnClickListener { listener.onItemClicked(position) }
    }

    class ViewHolder(itemView: View, private val context: Context): RecyclerView.ViewHolder(itemView){

        private val name: TextView = itemView.findViewById(R.id.tvProductNme)
        private val image: ImageView = itemView.findViewById(R.id.ivProductImage)

        fun setContent(product: Product){
            name.text = product.name

            GlideApp.with(context)
                .load(product.imageUrl)
                .into(image)
        }
    }

    interface OnItemClickedListener{
        fun onItemClicked(position: Int)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Product> = object : DiffUtil.ItemCallback<Product>(){
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}