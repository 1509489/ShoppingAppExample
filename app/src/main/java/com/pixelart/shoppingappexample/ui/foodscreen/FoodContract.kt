package com.pixelart.shoppingappexample.ui.foodscreen

import com.pixelart.shoppingappexample.base.BasePresenter
import com.pixelart.shoppingappexample.base.BaseView
import com.pixelart.shoppingappexample.model.FoodMain
import com.pixelart.shoppingappexample.model.Product

interface FoodContract {
    interface View: BaseView{
        fun showFoodProducts(foods: FoodMain)
    }
    interface Presenter: BasePresenter{
        fun getFoodProducts()
    }
}