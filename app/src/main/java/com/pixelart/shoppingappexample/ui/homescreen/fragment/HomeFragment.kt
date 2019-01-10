package com.pixelart.shoppingappexample.ui.homescreen.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.adapter.HomeRVAdapter
import com.pixelart.shoppingappexample.base.BaseFragment
import com.pixelart.shoppingappexample.common.Utils
import com.pixelart.shoppingappexample.model.Product
import com.pixelart.shoppingappexample.ui.homescreen.HomeContract
import com.pixelart.shoppingappexample.ui.homescreen.HomePresenter
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment<HomeContract.Presenter>(), HomeRVAdapter.OnItemClickedListener,HomeContract.View {

    private lateinit var adapter: HomeRVAdapter
    private lateinit var product: ArrayList<Product>
    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = HomeRVAdapter(this)
        product = ArrayList()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val spanCount = Utils.getNumberOfColumns(activity?.applicationContext!!)

        rootView.rvHome.layoutManager = GridLayoutManager(activity, spanCount)
        rootView.rvHome.addItemDecoration(DividerItemDecoration(activity, GridLayoutManager.VERTICAL))
        rootView.rvHome.addItemDecoration(DividerItemDecoration(activity, GridLayoutManager.HORIZONTAL))
        rootView.rvHome.adapter = adapter
        return rootView
    }

    override fun init() {
        presenter = HomePresenter(this)
        presenter.getFeaturedProducts()
    }

    override fun getViewPresenter(): HomeContract.Presenter = presenter

    override fun showFeaturedProducts(featuredProducts: List<Product>) {
        adapter.submitList(featuredProducts)
    }

    override fun onItemClicked(position: Int) {

    }

    companion object {
        const val ARG_FEATURED_PRODUCT = "featured_product"
    }

}
