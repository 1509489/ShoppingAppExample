package com.pixelart.shoppingappexample.common

import android.content.Context
import android.content.SharedPreferences
import com.pixelart.shoppingappexample.model.Customer

class SharedPreferencesManager private constructor(private val context: Context){

    private val PREF_NAME = "com.pixelart.shoppingappexample.sharedpreference"

    private val KEY_USERID = "userid"
    private val KEY_USERNAME = "username"
    private val KEY_FIRST_NAME = "firstname"
    private val KEY_LAST_NAME = "lastname"
    private val KEY_PHONE_NUMBER = "phonenumber"
    private val KEY_EMAIL = "email"

    fun onLogin(customer: Customer): Boolean{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(KEY_USERID, customer.id!!)
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

    fun getCustomer(): Customer{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return Customer(
            id = sharedPreferences.getInt(KEY_USERID, 0),
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

    companion object {
        @JvmStatic
        private var INSTANCE: SharedPreferencesManager? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): SharedPreferencesManager{
            if (INSTANCE == null)
            {
                INSTANCE = SharedPreferencesManager(context)
            }
            return INSTANCE as SharedPreferencesManager
        }
    }
}