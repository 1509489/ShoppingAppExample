package com.pixelart.shoppingappexample.ui.registerscreen

import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.remote.RemoteService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RegisterPresenter(private val remoteService: RemoteService, private val view: RegisterContract.View):
    RegisterContract.Presenter {
    override fun initiateRegister(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        userName: String,
        password: String
    ) {
        remoteService.registerUser(firstName, lastName,phoneNumber, email, userName, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Customer> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    view.showHideLoadingIndicator(true)
                }

                override fun onNext(t: Customer) {
                    view.showHideLoadingIndicator(false)
                    view.showMessage(t.message!!)
                    view.onRegisterSuccess(t)
                }

                override fun onError(e: Throwable) {
                    view.showError("Error: ${e.message}")
                }
            })
    }
}