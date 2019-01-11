package com.pixelart.shoppingappexample.ui.homescreen

import com.pixelart.shoppingappexample.model.FeaturedProducts
import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(private val view: HomeContract.View):
    HomeContract.Presenter {

    private val remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
    private var isLoaded = false

    override fun getFeaturedProducts() {
        remoteService.getFeaturedProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<FeaturedProducts> {
                override fun onSuccess(t: FeaturedProducts) {
                    view.showFeaturedProducts(t.products)
                }

                override fun onSubscribe(d: Disposable) {
                    isLoaded = true
                }

                override fun onError(e: Throwable) {
                    view.showError("Error ${e.message}")
                    e.printStackTrace()
                }
            })
    }

    override fun dataLoaded(): Boolean = isLoaded
}