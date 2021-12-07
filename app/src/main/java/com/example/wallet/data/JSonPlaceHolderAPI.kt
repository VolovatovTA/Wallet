package com.example.wallet.data

import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface JSonPlaceHolderAPI {


    // Transactions
    @GET("transaction/{id}")
    fun getTransaction(@Header("X-Auth-Token") accessToken: String, @Path("id") id: String): Call<myTransaction>

    @GET("transaction")
    fun getTransactions(@Header("X-Auth-Token") accessToken: String): Call<List<myTransaction>>

    @POST("transaction")
    fun createTransaction(@Header("X-Auth-Token") accessToken: String, @Body infoAboutTransaction: RequestBody): Call<myTransaction>

    @PUT("transaction/{id}")
    fun updateTransaction(@Header("X-Auth-Token") accessToken: String, @Body transaction: RequestBody, @Path("id") id: String): Call<String>

    @DELETE("transaction/{id}")
    fun deleteTransaction(@Header("X-Auth-Token") accessToken: String, @Path("id") id: String): Call<String>

    ////////////////////
    // Categories
    @GET("category/{id}")
    fun getCategory(@Header("X-Auth-Token") accessToken: String, @Path("id") id: Int): Call<CategoryTransaction>

    @GET("category")
    fun getCategories(@Header("X-Auth-Token") accessToken: String, @Path("id") id: Int): Call<List<CategoryTransaction>>

    @POST("category")
    fun createCategory(@Header("X-Auth-Token") accessToken: String, @Body categoryTransaction: RequestBody): Call<String>

    @PUT("category/{id}")
    fun updateCategory(@Header("X-Auth-Token") accessToken: String, @Body transaction: RequestBody, @Path("id") id: String)

    @DELETE("category/{id}")
    fun deleteCategory(@Header("X-Auth-Token") accessToken: String, @Path("id") id: String): Call<String>

    /////////////////////
    // Authorization
    @POST("auth/signUp")
    fun signUp(@Body credentials: RequestBody): Call<AuthTokens>


    @POST("auth/signIn")
    fun signIn(@Body credentials: RequestBody): Call<AuthTokens>

    @POST("auth/refresh")
    fun reSignIn(@Body refreshToken: RequestBody): Call<AuthTokens>
}
