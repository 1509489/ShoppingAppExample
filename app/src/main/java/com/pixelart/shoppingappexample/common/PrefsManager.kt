package com.pixelart.shoppingappexample.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.pixelart.shoppingappexample.model.Customer

enum class PrefsManager constructor(private var context: Application){
    INSTANCE;

    private constructor()

    fun onLogin(customer: Customer): Boolean{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(KEY_USER_ID, customer.id!!)
        editor.putString(KEY_FIRST_NAME, customer.firstname)
        editor.putString(KEY_LAST_NAME, customer.lastname)
        editor.putString(KEY_USERNAME, customer.username)
        editor.putString(KEY_EMAIL, customer.email)
        editor.putString(KEY_PHONE_NUMBER, customer.phonenumber)
        editor.apply()
        return true
    }

    fun isLoggedIn():Boolean{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getString(KEY_USERNAME, null) != null)
            return true
        return false
    }

    fun getCustomer(): Customer {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return Customer(
            id = sharedPreferences.getInt(KEY_USER_ID, 0),
            firstname = sharedPreferences.getString(KEY_FIRST_NAME, null),
            lastname = sharedPreferences.getString(KEY_LAST_NAME, null),
            phonenumber = sharedPreferences.getString(KEY_PHONE_NUMBER, null),
            email = sharedPreferences.getString(KEY_EMAIL, null),
            username = sharedPreferences.getString(KEY_USERNAME, null)
        )
    }

    fun onLogout(): Boolean{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        return true
    }

    fun setContext(ctx: Application){
        context = ctx
    }
}