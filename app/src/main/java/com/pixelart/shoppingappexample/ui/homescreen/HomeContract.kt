package com.pixelart.shoppingappexample.ui.homescreen

import com.pixelart.shoppingappexample.base.BasePresenter
import com.pixelart.shoppingappexample.base.BaseView
import com.pixelart.shoppingappexample.model.FeaturedProducts
import com.pixelart.shoppingappexample.model.Product

interface HomeContract{
    interface View: BaseView {
        fun showFeaturedProducts(featuredProducts: List<Product>)
    }

    interface Presenter: BasePresenter {
        fun getFeaturedProducts()
    }
}