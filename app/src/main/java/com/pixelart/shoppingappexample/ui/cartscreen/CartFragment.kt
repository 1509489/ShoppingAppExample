package com.pixelart.shoppingappexample.ui.cartscreen

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

import com.pixelart.shoppingappexample.ui.cartscreen.dummy.DummyContent.DummyItem

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
    private lateinit var cartItems: CartResponse

    private var listener: OnListFragmentInteractionListener? = null

    override fun init() {
        presenter = CartPresenter(this)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        showMessage("${PrefsManager.INSTANCE.getCustomer().id}")
        presenter.getCartItems("${PrefsManager.INSTANCE.getCustomer().id}")
        adapter = CartRecyclerViewAdapter(this)
        //cartItems = CartResponse()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                addItemDecoration(DividerItemDecoration(this@CartFragment.context, LinearLayoutManager.VERTICAL))
                adapter = this@CartFragment.adapter
            }
        }
        return view
    }

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }*/

    override fun onDetach() {
        super.onDetach()
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

    override fun showCartItem(cartResponse: CartResponse) {
        adapter.submitList(cartResponse.cartItems)
        cartItems = cartResponse//(cartResponse.cartItems as ArrayList<CartItem>)
    }

    override fun onItemClicked(position: Int) {
        showMessage("Item Clicked at: $position")
    }

    override fun onDeleteCartItem(position: Int) {
        presenter.deleteCartItem(cartItems.cartItems[position].id)

        adapter.notifyItemRemoved(position)
        presenter.getCartItems("${PrefsManager.INSTANCE.getCustomer().id}")
        showCartItem(cartItems)
        //adapter.submitList(cartItems.cartItems)
        //adapter.notifyDataSetChanged()
        adapter.notifyItemRangeChanged(position, adapter.itemCount)
        //adapter.notifyItemRemoved(position)

    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
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
