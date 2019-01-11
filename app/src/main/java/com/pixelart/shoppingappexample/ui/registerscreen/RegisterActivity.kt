package com.pixelart.shoppingappexample.ui.registerscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.pixelart.shoppingappexample.R
import com.pixelart.shoppingappexample.common.PrefsManager
import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.remote.RemoteHelper
import com.pixelart.shoppingappexample.remote.RemoteService
import com.pixelart.shoppingappexample.ui.MainActivity
import com.pixelart.shoppingappexample.ui.loginscreen.LoginActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var remoteService: RemoteService
    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        prefsManager.setContext(this)
        remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
    }

    fun registerUser(view: View){

        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val userName = etUserName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()

        remoteService.registerUser(firstName, lastName,phoneNumber, email, userName, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Customer>{
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Customer) {
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
                    if (t.message == "Registration success"){
                        finish()
                        //SharedPreferencesManager.getInstance(this@RegisterActivity).onLogin(t)
                        prefsManager.onLogin(t)
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    }
                }

                override fun onError(e: Throwable) {

                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                navigateUpTo(Intent(this, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
