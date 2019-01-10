package com.pixelart.shoppingappexample.ui.loginscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import com.pixelart.shoppingappexample.ui.homescreen.HomeActivity
import com.pixelart.shoppingappexample.ui.registerscreen.RegisterActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody

class LoginActivity : AppCompatActivity(){
    private lateinit var remoteService: RemoteService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun onClick(v: View) {
        val userName = etUsername.text.toString()
        val password = etPass.text.toString()
        var canLogin = ""

        remoteService.login(userName, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Customer> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Customer) {
                    tvDisplay.text = "Error: ${t.error}, Message: ${t.message}, \n${t.firstname}, ${t.lastname}, \n${t.username}, \n${t.email}, \n${t.phonenumber} "
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                    canLogin = t.message.toString()

                    if (t.message == "Login successful")
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })

        if(canLogin.contains("Login successful", false)){
            Toast.makeText(this@LoginActivity, "Can Login: $canLogin", Toast.LENGTH_SHORT).show()
        }
    }
}
