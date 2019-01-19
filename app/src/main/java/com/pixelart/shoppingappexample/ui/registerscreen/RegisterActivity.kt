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
import com.pixelart.shoppingappexample.ui.loginscreen.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View {
    private lateinit var remoteService: RemoteService
    private lateinit var presenter: RegisterPresenter
    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        remoteService = RemoteHelper.retrofitClient().create(RemoteService::class.java)
        presenter = RegisterPresenter(remoteService, this)
        prefsManager.setContext(this.application)
    }

    override fun showHideLoadingIndicator(isLoading: Boolean) {

    }

    override fun onRegisterSuccess(customer: Customer) {
        if (customer.message == "Registration success"){
            finish()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    fun registerUser(view: View){
        when(view.id){
            R.id.btnRegister ->{
                val firstName = etFirstName.text.toString()
                val lastName = etLastName.text.toString()
                val userName = etUserName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val phoneNumber = etPhoneNumber.text.toString()

                presenter.initiateRegister(firstName, lastName,phoneNumber, email, userName, password)
            }
        }
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
