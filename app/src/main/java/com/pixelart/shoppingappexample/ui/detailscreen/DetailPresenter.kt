package com.pixelart.shoppingappexample.ui.detailscreen

import com.pixelart.shoppingappexample.common.RxBus
import com.pixelart.shoppingappexample.model.Product
import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class DetailPresenter(private val view: DetailContract.View):DetailContract.Presenter {

    private var disposable: Disposable? = null
    private val remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)

    override fun getDetails() {
        disposable = RxBus.INSTANCE.subscribe(Consumer {
            if (it is Product){
                view.showDetails(it)
            }
        })
    }

    override fun addToCart(name: String, description: String, quantity: String, price: String,
        imgUrl: String, customerId: String, productId: String) {

        remoteService.addToCart(name, description, quantity, price, imgUrl, customerId, productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { response -> view.showMessage(response.message) }
            .doOnError { t -> view.showError(t.message!!) }
            .subscribe()
    }

    override fun onDestroy() {
        disposable?.dispose()
    }
}