package com.pixelart.shoppingappexample.remote

import com.pixelart.shoppingappexample.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
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

    //Get the products for the food screen
    @GET("v1/foodmain.php")
    fun getFoodProducts():Single<FoodMain>

    //Adding item to cart
    @FormUrlEncoded
    @POST("v1/cartinsert.php")
    fun addToCart(
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("quantity") quantity: String,
        @Field("price") price: String,
        @Field("img_url") imgUrl: String,
        @Field("customer") customerId: String,
        @Field("product_id") productId: String
    ):Observable<CartResponse>

    //Get cart items for the logged in user
    @FormUrlEncoded
    @POST("v1/cartretrieve.php")
    fun getCartItems(@Field("customer") customerId: String):Observable<CartResponse>

    //Delete item from cart
    @FormUrlEncoded
    @POST("v1/cartdelete.php")
    fun deleteCartItem(@Field("id") itemId: String,
                       @Field("customer") customerId: String
    ):Observable<DefaultResponse>

    //Set cart item quantity
    @FormUrlEncoded
    @POST("v1/setquantity.php")
    fun setQuantity(@Field("quantity") quantity: String,
                    @Field("customer") customerId: String,
                    @Field("product_id") productId: String
    ):Observable<CartResponse>

    //Add order
    @FormUrlEncoded
    @POST("v1/orderinsert.php")
    fun addOrder(
        @Field("customer_id")customerId: String,
        @Field("total_price")totalPrice: String,
        @Field("total_items")totalItems: String
    ):Observable<OrderNumberResponse>

    //Add order details
    @FormUrlEncoded
    @POST("v1/orderdetailinsert.php")
    fun addOrderDetails(
        @Field("order_number")orderNumber: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("quantity") quantity: String,
        @Field("price") price: String,
        @Field("img_url") imgUrl: String
    ):Observable<DefaultResponse>
    
    //Get orders for customer
    @FormUrlEncoded
    @POST("v1/ordersretrieve.php")
    fun getOrders(@Field("customer_id") customerId: String):Observable<OrdersResponse>
    
    //Get Order details
    @FormUrlEncoded
    @POST("v1/orderdetailsretrieve.php")
    fun getOrderDetails(@Field("order_number") orderNumber: String):Observable<OrderDetailResponse>

    @GET("braintree/main.php")
    fun getBraintreeToken(): Single<Response<String>>

    @FormUrlEncoded
    @POST("braintree/checkout.php")
    fun checkout(
        @Field("nounce") nounce: String,
        @Field("amount") amount: String
    ): Observable<String>
}