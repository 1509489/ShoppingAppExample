package com.pixelart.shoppingappexample.ui.homescreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager

import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.adapter.HomeRVAdapter
import com.pixelart.shoppingappexample.base.BaseFragment
import com.pixelart.shoppingappexample.common.Utils
import com.pixelart.shoppingappexample.model.Product
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment<HomeContract.Presenter>(), HomeRVAdapter.OnItemClickedListener,
    HomeContract.View {

    private lateinit var adapter: HomeRVAdapter
    private lateinit var product: ArrayList<Product>
    private lateinit var presenter: HomePresenter
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = HomeRVAdapter(this)
        product = ArrayList()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        if (!presenter.dataLoaded()) showLoadingIndicator(rootView.pbHome)
        else hideLoadingIndicator(rootView.pbHome)

        val spanCount = Utils.getNumberOfColumns(activity?.applicationContext!!)
        val spacingInpixel = 2
        val gridDecoration = Utils.GridItemDecorator(spacingInpixel, spanCount, true)

        val layoutManager = GridLayoutManager(activity, spanCount)

        rootView.rvHome.layoutManager = layoutManager
        rootView.rvHome.addItemDecoration(gridDecoration)
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
        if (presenter.dataLoaded())
            hideLoadingIndicator(rootView.pbHome)
    }

    override fun onItemClicked(position: Int) {

    }

    fun getLoadingIndicator():ProgressBar = rootView.pbHome

    companion object {
        const val ARG_FEATURED_PRODUCT = "featured_product"
    }

}
