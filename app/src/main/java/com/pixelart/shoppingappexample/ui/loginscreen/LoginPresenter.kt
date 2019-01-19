package com.pixelart.shoppingappexample.ui.loginscreen

import android.content.Intent
import android.widget.Toast
import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.remote.RemoteService
import com.pixelart.shoppingappexample.ui.MainActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private val remoteService: RemoteService, private val view: LoginContract.View):LoginContract.Presenter {
    override fun initiateLogIn(username: String, password: String) {
        remoteService.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Customer> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    view.showHideLoadingIndicator(true)
                }

                override fun onNext(t: Customer) {
                    view.showMessage(t.message!!)
                    view.showHideLoadingIndicator(false)
                    view.onLoginSuccess(t)
                }

                override fun onError(e: Throwable) {
                    view.showError("Error: ${e.message}")
                }
            })
    }
}