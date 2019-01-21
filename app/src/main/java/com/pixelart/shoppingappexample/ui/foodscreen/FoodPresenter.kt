package com.pixelart.shoppingappexample.ui.foodscreen

import androidx.annotation.NonNull
import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FoodPresenter( private val view: FoodContract.View):
    FoodContract.Presenter {
    private val compositeDisposable = CompositeDisposable()
    private val remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)

    override fun getFoodProducts() {
        compositeDisposable.add(
            remoteService.getFoodProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showProgress() }
            .doOnEvent { _, _ ->  view.hideProgress() }
            .subscribe(view::showFoodProducts, this::handleError)
        )
    }

    override fun addToCart(
        name: String,
        description: String,
        quantity: String,
        price: String,
        imgUrl: String,
        customerId: String,
        productId: String
    ) {
        remoteService.addToCart(name, description, quantity, price, imgUrl, customerId, productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { cartItem -> view.showMessage(cartItem.message) }
            .doOnError { t -> view.showError(t.message!!) }
            .subscribe()
    }

    override fun onDestroy() {
        compositeDisposable.size()
    }

    private fun handleError(@NonNull t: Throwable){
        if (t.message !=null){
            view.showError(t.message!!)
            t.printStackTrace()
        }
    }
}