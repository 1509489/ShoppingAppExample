package com.pixelart.shoppingappexample.ui.homescreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager

import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.adapter.HomeRVAdapter
import com.pixelart.shoppingappexample.base.BaseFragment
import com.pixelart.shoppingappexample.common.RxBus
import com.pixelart.shoppingappexample.common.Utils
import com.pixelart.shoppingappexample.model.Product
import com.pixelart.shoppingappexample.ui.detailscreen.DetailFragment
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment<HomeContract.Presenter>(), HomeRVAdapter.OnItemClickedListener,
    HomeContract.View {

    private lateinit var adapter: HomeRVAdapter
    private lateinit var products: ArrayList<Product>
    private lateinit var presenter: HomePresenter
    private lateinit var rootView: View

    override fun init() {
        presenter = HomePresenter(this)
        presenter.getFeaturedProducts()
        adapter = HomeRVAdapter(this)
        products = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
/*
        if (!presenter.dataLoaded()) showLoadingIndicator(rootView.pbHome)
        else hideLoadingIndicator(rootView.pbHome)*/
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView.pbHome.visibility = View.GONE

        val spanCount = Utils.getNumberOfColumns(activity?.applicationContext!!)
        val spacingInPixel = 2
        val gridDecoration = Utils.GridItemDecorator(spacingInPixel, spanCount, true)

        val layoutManager = GridLayoutManager(activity, spanCount)

        rootView.rvHome.layoutManager = layoutManager
        rootView.rvHome.addItemDecoration(gridDecoration)
        rootView.rvHome.adapter = adapter
    }

    override fun getViewPresenter(): HomeContract.Presenter = presenter

    override fun showFeaturedProducts(featuredProducts: List<Product>) {
        adapter.submitList(featuredProducts)
        products = featuredProducts as ArrayList<Product>
        if (presenter.dataLoaded())
            hideLoadingIndicator(rootView.pbHome)
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onItemClicked(position: Int) {
        RxBus.INSTANCE.post(products[position])
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.home_content, DetailFragment())
            ?.addToBackStack("home_fragment")
            ?.commit()
    }

    fun getLoadingIndicator():ProgressBar = rootView.pbHome
}
