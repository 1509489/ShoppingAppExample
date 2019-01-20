package com.pixelart.shoppingappexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.common.GlideApp
import com.pixelart.shoppingappexample.model.Product

class FoodListAdpater(private val listener: OnItemClickedListener): ListAdapter<Product, FoodListAdpater.ViewHolder>(DIFF_CALLBACK) {


    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListAdpater.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.food_rv_layout, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: FoodListAdpater.ViewHolder, position: Int) {
        val product = getItem(position)
        holder.apply { setContent(product)
            btnAddToCart.setOnClickListener { listener.onAddToCart(position) }
        }

        holder.itemView.setOnClickListener { listener.onItemClicked(position) }
    }

    class ViewHolder(itemView: View, private val context: Context): RecyclerView.ViewHolder(itemView){

        private val name: TextView = itemView.findViewById(R.id.tvProductName)
        private val description: TextView = itemView.findViewById(R.id.tvProductDesc)
        private val price: TextView = itemView.findViewById(R.id.tvPrice)
        val btnAddToCart: ImageButton = itemView.findViewById(R.id.ibAddToCart)
        private val image: ImageView = itemView.findViewById(R.id.ivFoodImage)

        fun setContent(product: Product){
            name.text = product.name
            description.text = product.description
            price.text = "£${product.price}"


            GlideApp.with(context)
                .load(product.imageUrl)
                .into(image)
        }
    }

    interface OnItemClickedListener{
        fun onItemClicked(position: Int)
        fun onAddToCart(position: Int)
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