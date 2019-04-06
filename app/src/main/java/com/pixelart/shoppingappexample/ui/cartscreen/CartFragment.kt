package com.pixelart.shoppingappexample.ui.cartscreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.adapter.CartRecyclerViewAdapter
import com.pixelart.shoppingappexample.base.BaseFragment
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.model.CartItem
import com.pixelart.shoppingappexample.model.CartResponse
import com.pixelart.shoppingappexample.model.OrderNumberResponse
import kotlinx.android.synthetic.main.cart_rv_layout.view.*
import kotlinx.android.synthetic.main.fragment_cart.view.*
import java.util.*

class CartFragment : BaseFragment<CartContract.Presenter>(), CartContract.View, CartRecyclerViewAdapter.OnItemClickedListener {

    // TODO: Customize parameters
    private var columnCount = 1

    private lateinit var presenter: CartPresenter
    private lateinit var adapter: CartRecyclerViewAdapter
    private lateinit var rootView: View
    private var cartItems: ArrayList<CartItem>? = null

    override fun init() {
        val actionBar = activity?.actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        presenter = CartPresenter(this)
        //showMessage("${PrefsManager.INSTANCE.getCustomer().id}")
        presenter.getCartItems("${PrefsManager.INSTANCE.getCustomer().id}")
        presenter.getBraintreeToken()
        cartItems = ArrayList()
        adapter = CartRecyclerViewAdapter(this)
        //cartItems = CartResponse()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_cart, container, false)

        // Set the adapter
        with(rootView.rvCart) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            addItemDecoration(DividerItemDecoration(this@CartFragment.context, LinearLayoutManager.VERTICAL))
            adapter = this@CartFragment.adapter
        }

        rootView.titleSubtotal.text = "${activity?.resources?.getString(R.string.basket_subtotal)} (${totalItems()} items):"
        rootView.tvSubtotal.text = totalPrice().toString()

        rootView.btnCheckout.setOnClickListener {
            val customerId = PrefsManager.INSTANCE.getCustomer().id
            presenter.addOrder(customerId.toString(), totalPrice().toString(), totalItems().toString())
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Cart", "Fragment attached")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Cart", "Fragment Detach")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun getViewPresenter(): CartContract.Presenter = presenter

    override fun showProgress() {

    }

    override fun hideProgress() {

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

        rootView.titleSubtotal.text = "${activity?.resources?.getString(R.string.basket_subtotal)} ($totalItems items):"
        rootView.tvSubtotal.text = totalPrice

        adapter.notifyDataSetChanged()
    }

    override fun onItemClicked(position: Int) {
        showMessage("Item Clicked at: $position")
    }

    override fun onDeleteCartItem(position: Int) {
        presenter.deleteCartItem(cartItems!![position].id, "${PrefsManager.INSTANCE.getCustomer().id}")
        //cartItems?.removeAt(position)//Remove list item to match the size in the database
        fragmentManager?.popBackStackImmediate("cart_fragment", 0)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.home_content, CartFragment())
            ?.addToBackStack("cart_fragment")
            ?.commit()

        //adapter.notifyDataSetChanged()


        Log.d("Cart", "Item Size: ${cartItems?.size}, Adapter count: ${adapter.itemCount}, Position Removed: $position")
    }

    override fun onQuantityChange(position: Int, quantity: CharSequence?) {
        if (quantity != null){
            presenter.setQuantity("$quantity", "${PrefsManager.INSTANCE.getCustomer().id}",
                    cartItems!![position].productId)
        }
        subtotal()
    }

    @SuppressLint("SetTextI18n")
    private fun subtotal(){
        var totalItems = 0
        var pricesSum = 0.0
        for (i in 0 until adapter.itemCount) {
            val item = cartItems!![i]
            val quantity = rootView.rvCart.findViewHolderForAdapterPosition(i)
                    ?.itemView?.etQuantity?.text.toString()

            totalItems += quantity.toInt()
            val price = rootView.rvCart.findViewHolderForAdapterPosition(i)
                    ?.itemView?.tvPrice?.text.toString()

            pricesSum += if (quantity.toInt() > 1){
                price.toDouble() * quantity.toInt()
            }else{
                item.price.toDouble()
            }
        }
        val totalPrice = String.format("%.2f",pricesSum)

        rootView.titleSubtotal.text = "${activity?.resources?.getString(R.string.basket_subtotal)} ($totalItems items):"
        rootView.tvSubtotal.text = totalPrice

    }

    private fun totalPrice():Double{
        var pricesSum = 0.0

        for (i in 0 until adapter.itemCount) {
            val item = cartItems!![i]
            val quantity = rootView.rvCart.findViewHolderForAdapterPosition(i)
                ?.itemView?.etQuantity?.text.toString()

            val price = rootView.rvCart.findViewHolderForAdapterPosition(i)
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
            val quantity = rootView.rvCart.findViewHolderForAdapterPosition(i)
                ?.itemView?.etQuantity?.text.toString()

            totalItems += quantity.toInt()
        }

        return totalItems
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

            fragmentManager?.beginTransaction()
                ?.detach(CartFragment())
                ?.attach(CartFragment())
                ?.commit()
        }
    }

    override fun getBraintreeToken(token: String) {
        Toast.makeText(activity, "Braintree Token: $token", Toast.LENGTH_SHORT).show()
    }
}
