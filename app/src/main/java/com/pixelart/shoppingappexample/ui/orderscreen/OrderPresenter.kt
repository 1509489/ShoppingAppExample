package com.pixelart.shoppingappexample.ui.orderscreen

import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import io.reactivex.disposables.CompositeDisposable

class OrderPresenter(private val view: OrderContract.View) : OrderContract.Presenter{
    private val remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.clear()
    }
}