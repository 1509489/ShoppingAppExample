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
import androidx.recyclerview.widget.DividerItemDecoration
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.adapter.CartRecyclerViewAdapter
import com.pixelart.shoppingappexample.base.BaseFragment
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.model.CartItem
import com.pixelart.shoppingappexample.model.CartResponse
import kotlinx.android.synthetic.main.cart_rv_layout.view.*
import kotlinx.android.synthetic.main.fragment_cart.view.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [CartFragment.OnListFragmentInteractionListener] interface.
 */
class CartFragment : BaseFragment<CartContract.Presenter>(), CartContract.View, CartRecyclerViewAdapter.OnItemClickedListener {

    // TODO: Customize parameters
    private var columnCount = 1

    private lateinit var presenter: CartPresenter
    private lateinit var adapter: CartRecyclerViewAdapter
    private lateinit var rootView: View
    private var cartItems: ArrayList<CartItem>? = null

    private var listener: OnListFragmentInteractionListener? = null

    override fun init() {
        val actionBar = activity?.actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        presenter = CartPresenter(this)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        //showMessage("${PrefsManager.INSTANCE.getCustomer().id}")
        presenter.getCartItems("${PrefsManager.INSTANCE.getCustomer().id}")
        cartItems = ArrayList()
        adapter = CartRecyclerViewAdapter(this)
        //cartItems = CartResponse()
    }

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

        rootView.btnCheckout.setOnClickListener {
            /*activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_content, CartFragment())
                ?.addToBackStack(null)
                ?.commit()*/
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Cart", "Fragment attached")
        /*if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }*/
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Cart", "Fragment Detach")
        listener = null
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

        var totalItems = 0
        var pricesSum = 0.0
        for (i in 0 until cartResponse?.cartItems!!.size){
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
        cartItems = (cartResponse.cartItems as ArrayList<CartItem>)
    }

    override fun onItemClicked(position: Int) {
        showMessage("Item Clicked at: $position")
    }

    override fun onDeleteCartItem(position: Int) {
        presenter.deleteCartItem(cartItems!![position].id, "${PrefsManager.INSTANCE.getCustomer().id}")
        cartItems?.removeAt(position)//Remove list item to match the size in the database
        adapter.notifyItemRemoved(position)

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



    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: CartItem)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
