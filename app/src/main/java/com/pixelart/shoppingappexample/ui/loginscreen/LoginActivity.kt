package com.pixelart.shoppingappexample.ui.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import com.pixelart.shoppingappexample.ui.MainActivity
import com.pixelart.shoppingappexample.ui.registerscreen.RegisterActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){
    private lateinit var remoteService: RemoteService
    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefsManager.setContext(this)

        if (prefsManager.isLoggedIn()){
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun onClick(v: View) {
        val userName = etUsername.text.toString()
        val password = etPass.text.toString()

        remoteService.login(userName, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Customer> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Customer) {
                    if (t.message == "Login successful"){
                        finish()
                        prefsManager.onLogin(t)
                        //SharedPreferencesManager.getInstance(this@LoginActivity).onLogin(t)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
