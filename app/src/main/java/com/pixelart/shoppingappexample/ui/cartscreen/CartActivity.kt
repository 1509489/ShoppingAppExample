package com.pixelart.shoppingappexample.ui.cartscreen

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.adapter.CartRecyclerViewAdapter
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.model.CartItem
import com.pixelart.shoppingappexample.model.CartResponse
import com.pixelart.shoppingappexample.model.OrderNumberResponse
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_rv_layout.view.*
import java.util.ArrayList

class CartActivity : AppCompatActivity(),CartContract.View, CartRecyclerViewAdapter.OnItemClickedListener {

    private var columnCount = 1

    private lateinit var presenter: CartPresenter
    private lateinit var adapter: CartRecyclerViewAdapter
    private var cartItems: ArrayList<CartItem>? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter = CartPresenter(this)
        presenter.getCartItems("${PrefsManager.INSTANCE.getCustomer().id}")
        cartItems = ArrayList()
        adapter = CartRecyclerViewAdapter(this)

        // Set the adapter
        with(rvCart) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            addItemDecoration(DividerItemDecoration(this@CartActivity, LinearLayoutManager.VERTICAL))
            adapter = this@CartActivity.adapter
        }

        titleSubtotal.text = "${resources?.getString(R.string.basket_subtotal)} (${totalItems()} items):"
        tvSubtotal.text = totalPrice().toString()

        btnCheckout.setOnClickListener {
            val customerId = PrefsManager.INSTANCE.getCustomer().id
            presenter.addOrder(customerId.toString(), totalPrice().toString(), totalItems().toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    override fun showCartItem(cartResponse: CartResponse?) {
        adapter.submitList(cartResponse?.cartItems)

        if (cartResponse != null)
            cartItems = (cartResponse.cartItems as ArrayList<CartItem>)

        var totalItems = 0
        var pricesSum = 0.0
        for (i in 0 until cartResponse?.cartItems?.size!!){
            val item = cartResponse.cartItems[i]

            totalItems += item.quantity.toInt()

            pricesSum += if (item.quantity.toInt() > 1){
                item.price.toDouble() * item.quantity.toInt()
            }else{
                item.price.toDouble()
            }
        }

        val totalPrice = String.format("%.2f",pricesSum)

        titleSubtotal.text = "${resources?.getString(R.string.basket_subtotal)} ($totalItems items):"
        tvSubtotal.text = totalPrice

        adapter.notifyDataSetChanged()
    }

    override fun getOrderNumber(orderResponse: OrderNumberResponse) {
        if (!orderResponse.error){
            for (i in 0 until cartItems?.size!!){
                presenter.addOrderDetails(
                    orderResponse.orderNumber,
                    cartItems!![i].name,
                    cartItems!![i].description,
                    cartItems!![i].quantity,
                    cartItems!![i].price,
                    cartItems!![i].imageUrl
                )
            }
            for (i in 0 until cartItems?.size!!){
                presenter.deleteCartItem(cartItems!![i].id, PrefsManager.INSTANCE.getCustomer().id.toString())
            }

            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingIndicator(loadingIndicator: ProgressBar) {

    }

    override fun hideLoadingIndicator(loadingIndicator: ProgressBar) {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onItemClicked(position: Int) {

    }

    override fun onDeleteCartItem(position: Int) {
        presenter.deleteCartItem(cartItems!![position].id, "${PrefsManager.INSTANCE.getCustomer().id}")

        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onQuantityChange(position: Int, quantity: CharSequence?) {
        if (quantity != null){
            presenter.setQuantity("$quantity", "${PrefsManager.INSTANCE.getCustomer().id}",
                cartItems!![position].productId)
        }
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun totalPrice():Double{
        var pricesSum = 0.0

        for (i in 0 until adapter.itemCount) {
            val item = cartItems!![i]
            val quantity = rvCart.findViewHolderForAdapterPosition(i)
                ?.itemView?.etQuantity?.text.toString()

            val price = rvCart.findViewHolderForAdapterPosition(i)
                ?.itemView?.tvPrice?.text.toString()

            pricesSum += if (quantity.toInt() > 1){
                price.toDouble() * quantity.toInt()
            }else{
                item.price.toDouble()
            }
        }

        return String.format("%.2f",pricesSum).toDouble()
    }

    private fun totalItems():Int{

        var totalItems = 0
        for (i in 0 until adapter.itemCount) {
            val quantity = rvCart.findViewHolderForAdapterPosition(i)
                ?.itemView?.etQuantity?.text.toString()

            totalItems += quantity.toInt()
        }

        return totalItems
    }
}
