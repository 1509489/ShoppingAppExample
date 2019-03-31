package com.pixelart.shoppingappexample.ui.orderscreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.adapter.OrdersAdapter
import com.pixelart.shoppingappexample.base.BaseFragment
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.model.OrdersResponse
import kotlinx.android.synthetic.main.fragment_orders.view.*


class OrdersFragment : BaseFragment<OrderContract.Presenter>(),OrderContract.View,
    OrdersAdapter.OnItemClickedListener {
    
    private lateinit var presenter: OrderPresenter
    private lateinit var rootView: View
    private lateinit var adapter: OrdersAdapter
    
    override fun init() {
        activity?.title = "Orders"
        presenter = OrderPresenter(this)
        presenter.getOrders(PrefsManager.INSTANCE.getCustomer().id.toString())
        adapter = OrdersAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_orders, container, false)
        
        rootView.rvOrders.apply {
            layoutManager = LinearLayoutManager(this@OrdersFragment.context)
            addItemDecoration(DividerItemDecoration(this@OrdersFragment.context, LinearLayoutManager.VERTICAL))
            adapter = this@OrdersFragment.adapter
        }
        
        return rootView
    }
    
    override fun getViewPresenter(): OrderContract.Presenter = presenter

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
    
    override fun showOrders(ordersResponse: OrdersResponse) {
        adapter.submitList(ordersResponse.orders)
    }
    
    override fun onItemClicked(position: Int) {
    
    }
}
