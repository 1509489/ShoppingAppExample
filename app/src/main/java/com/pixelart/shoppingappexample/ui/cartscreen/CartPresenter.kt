package com.pixelart.shoppingappexample.ui.cartscreen

import androidx.annotation.NonNull
import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CartPresenter(private val view: CartContract.View): CartContract.Presenter {

    private val remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
    private val compositeDisposable = CompositeDisposable()

    override fun getCartItems(customerId: String) {
        compositeDisposable.add(
            remoteService.getCartItems(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { response -> view.showMessage(response.message)}
                .subscribe(view::showCartItem, this::handleError)
        )
    }

    override fun deleteCartItem(itemId: String) {
        remoteService.deleteCartItem(itemId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { response -> view.showMessage(response.message)}
            .doOnError { t -> view.showError(t.message!!) }
            .subscribe()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    private fun handleError(@NonNull t: Throwable){
        if (t.message !=null){
            view.showError(t.message!!)
            t.printStackTrace()
        }
    }
}