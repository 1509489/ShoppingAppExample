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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View{
    private lateinit var remoteService: RemoteService
    private lateinit var presenter: LoginPresenter
    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
        presenter = LoginPresenter(remoteService,this)
        prefsManager.setContext(this.application)

        if (prefsManager.isLoggedIn()){
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun showHideLoadingIndicator(isLoading: Boolean) {

    }

    override fun onLoginSuccess(customer: Customer) {
        if (customer.message == "Login successful"){
            finish()
            prefsManager.onLogin(customer)
            //SharedPreferencesManager.getInstance(this@LoginActivity).onLogin(t)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    fun onClick(v: View) {
        when(v.id) {
            R.id.btnLogin ->{
                val userName = etUsername.text.toString()
                val password = etPass.text.toString()

                presenter.initiateLogIn(userName, password)
            }
        }
    }
}
