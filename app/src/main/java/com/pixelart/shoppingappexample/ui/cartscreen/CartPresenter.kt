package com.pixelart.shoppingappexample.ui.cartscreen

import androidx.annotation.NonNull
import com.pixelart.shoppingappexample.model.CartResponse
import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class CartPresenter(private val view: CartContract.View): CartContract.Presenter {

    private val remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
    private val compositeDisposable = CompositeDisposable()

    override fun getCartItems(customerId: String) {
        compositeDisposable.add(
            remoteService.getCartItems(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.doOnNext { response -> view.showMessage(response.message)}
                .subscribe(view::showCartItem, this::handleError)
        )
    }

    override fun deleteCartItem(itemId: String, customerId: String) {
        compositeDisposable.add(
            remoteService.deleteCartItem(itemId, customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { response -> view.showMessage(response.message)}
                .doOnError { t -> view.showError(t.message!!) }
                .subscribe()
        )
    }

    override fun setQuantity(quantity: String, customerId: String, productId: String) {
        compositeDisposable.add(
            remoteService.setQuantity(quantity, customerId, productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /*.doOnNext { response -> view.showMessage(response.message) }
                .doOnError { t -> view.showError(t.message!!) }*/
                .subscribe(this::handleQuantitySuccess, this::handleError)
        )
    }

    override fun addOrder(customerId: String, totalPrice: String, totalItems: String) {
        compositeDisposable.add(
            remoteService.addOrder(customerId, totalPrice, totalItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::getOrderNumber, this::handleError)
        )
    }

    override fun addOrderDetails(orderNumber: String, name: String, description: String,
                                 quantity: String, totalPrice: String, imgUrl: String) {

        compositeDisposable.add(
            remoteService.addOrderDetails(orderNumber, name, description, quantity, totalPrice, imgUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun getBraintreeToken() {
        /*compositeDisposable.add(

        )*/
        remoteService.getBraintreeToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Response<String>>{
                override fun onSuccess(t: Response<String>) {
                    t.body()?.let { view.getBraintreeToken(it) }
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    handleError(e)
                }
            })
    }

    override fun checkout(nounce: String, amount: String) {
        compositeDisposable.add(
            remoteService.checkout(nounce, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )

    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    private fun handleError(@NonNull t: Throwable){
        if (t.message !=null){
            //view.showError(t.message!!)
            t.printStackTrace()
        }
    }

    private fun handleQuantitySuccess(response: CartResponse){
        view.showMessage(response.message)
    }
}