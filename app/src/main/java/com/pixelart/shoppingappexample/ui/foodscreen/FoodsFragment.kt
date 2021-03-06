package com.pixelart.shoppingappexample.ui.foodscreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.adapter.FoodListAdpater
import com.pixelart.shoppingappexample.base.BaseFragment
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.common.RxBus
import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.model.FoodMain
import com.pixelart.shoppingappexample.model.Product
import com.pixelart.shoppingappexample.ui.detailscreen.DetailFragment
import kotlinx.android.synthetic.main.fragment_foods.view.*

class FoodsFragment : BaseFragment<FoodContract.Presenter>(), FoodContract.View,FoodListAdpater.OnItemClickedListener {
    private lateinit var rootView: View
    private lateinit var presenter: FoodPresenter
    private lateinit var adapter: FoodListAdpater
    private lateinit var foods: ArrayList<Product>
    private lateinit var customer: Customer

    private val prefsManager = PrefsManager.INSTANCE

    override fun init() {
        activity?.title = "Foods"
        presenter = FoodPresenter(this)
        presenter.getFoodProducts()
        adapter = FoodListAdpater(this)
        foods = ArrayList()

        prefsManager.setContext(activity?.application!!)
        customer = prefsManager.getCustomer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_foods, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView.rvFoods.layoutManager = LinearLayoutManager(activity)
        rootView.rvFoods.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        rootView.rvFoods.adapter = adapter
        rootView.pbFoods.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun getViewPresenter(): FoodContract.Presenter = presenter

    override fun showProgress() {
        if (::rootView.isInitialized)
            rootView.pbFoods.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        if (::rootView.isInitialized)
            rootView.pbFoods.visibility = View.GONE
    }

    override fun showFoodProducts(foods: FoodMain) {
        adapter.submitList(foods.products)
        this.foods = (foods.products as ArrayList<Product>)
    }

    override fun onItemClicked(position: Int) {
        RxBus.INSTANCE.post(foods[position])
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.home_content, DetailFragment())
            ?.addToBackStack("foods_fragment")
            ?.commit()
    }

    override fun onAddToCart(position: Int) {
        val quantity = 1
        presenter.addToCart(foods[position].name, foods[position].description, "$quantity", foods[position].price,
            foods[position].imageUrl, "${customer.id}", foods[position].id)
    }
}
