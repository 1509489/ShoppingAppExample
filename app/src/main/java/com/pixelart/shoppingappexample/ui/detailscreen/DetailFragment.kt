package com.pixelart.shoppingappexample.ui.detailscreen


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation

import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.base.BaseFragment
import com.pixelart.shoppingappexample.common.GlideApp
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.model.Product
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailFragment : BaseFragment<DetailContract.Presenter>(), DetailContract.View,View.OnClickListener {
    private lateinit var presenter: DetailPresenter
    private lateinit var rootView: View
    private lateinit var product: Product
    private var quantity = 1
    private lateinit var customer: Customer

    override fun init() {
        presenter = DetailPresenter(this)
        presenter.getDetails()
        customer = PrefsManager.INSTANCE.getCustomer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_detail, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.apply {
            tvName.text = product.name
            tvDescription.text = product.description
            tvPrice.text = product.price
            etQuantity.setText("$quantity")
            ibQtyDecrease.setOnClickListener(this@DetailFragment)
            ibQtyIncrease.setOnClickListener(this@DetailFragment)
            btnAddToCart.setOnClickListener(this@DetailFragment)

            etQuantity.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != ""){
                        quantity = s.toString().toInt()
                        showMessage("$quantity")
                    }
                    if(s.toString() == "0"){
                        showMessage("Quantity cannot be zero")
                        rootView.etQuantity.startAnimation(shakeOnError())
                    }
                }
            })
        }

        GlideApp.with(this)
            .load(product.imageUrl)
            .into(rootView.ivImage)
    }

    override fun onClick(view: View) {
        when(view){
            rootView.ibQtyIncrease ->{
                quantity++
                rootView.etQuantity.setText("$quantity")
            }
            rootView.ibQtyDecrease ->{
                if (quantity > 1){
                    quantity--
                    rootView.etQuantity.setText("$quantity")
                }
            }
            rootView.btnAddToCart ->{
                presenter.addToCart(product.name, product.description, etQuantity.text.toString(),
                    product.price, product.imageUrl, customer.id.toString(), product.id)
            }
        }
    }

    override fun getViewPresenter(): DetailContract.Presenter = presenter

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun showDetails(product: Product) {
        activity?.title = product.name
        this.product = product
    }

    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    private fun shakeOnError(): TranslateAnimation {
        val shake = TranslateAnimation(0f, 10f, 0f, 0f)
        shake.duration = 500
        shake.interpolator = CycleInterpolator(7f)
        return shake
    }
}
