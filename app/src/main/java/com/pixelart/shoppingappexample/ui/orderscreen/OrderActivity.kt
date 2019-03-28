package com.pixelart.shoppingappexample.ui.orderscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.pixelart.shoppingappexample.R

class OrderActivity : AppCompatActivity(),OrderContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingIndicator(loadingIndicator: ProgressBar) {

    }

    override fun hideLoadingIndicator(loadingIndicator: ProgressBar) {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}
