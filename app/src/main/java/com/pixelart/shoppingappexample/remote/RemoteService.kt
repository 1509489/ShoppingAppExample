package com.pixelart.shoppingappexample.remote

import com.pixelart.shoppingappexample.model.Customer
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RemoteService{
    @FormUrlEncoded
    @POST("v1/register.php")
    fun registerUser(
        @Field("firstname") firstName: String,
        @Field("lastname") lastName: String,
        @Field("phonenumber") phoneNumber: String,
        @Field("email") email: String,
        @Field("username") userName: String,
        @Field("password") password: String
    ): Observable<Customer>

    @FormUrlEncoded
    @POST("v1/login.php")
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Observable<Customer>
}