package com.pixelart.shoppingappexample.remote

import com.pixelart.shoppingappexample.model.Customer
import com.pixelart.shoppingappexample.model.FeaturedProducts
import com.pixelart.shoppingappexample.model.Product
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteService{

    //User registration request
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

    //User login request
    @FormUrlEncoded
    @POST("v1/login.php")
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Observable<Customer>

    //Get home screen products request
    @GET("v1/featuredproducts.php")
    fun getFeaturedProducts():Single<FeaturedProducts>
}