package com.pixelart.shoppingappexample.ui.orderscreen

import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OrderPresenter(private val view: OrderContract.View) : OrderContract.Presenter{
    private val remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
    private val compositeDisposable = CompositeDisposable()
    
    override fun getOrders(customerId: String) {
        compositeDisposable.add(
            remoteService.getOrders(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showOrders, this::handleError)
        )
    }
    
    private fun handleError(throwable: Throwable){
        if (throwable.message != null){
            view.showError(throwable.message ?: "Some error occurred")
        }
    }
    
    override fun onDestroy() {
        compositeDisposable.clear()
    }
}