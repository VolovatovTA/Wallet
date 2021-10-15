package com.example.wallet.data

import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface JSonPlaceHolderAPI {
    // Transactions
    @GET("transaction/{id}")
    fun getTransaction(@Header("Authorization") accessToken: String, @Path("id") id: String): Call<myTransaction>

    @GET("transaction")
    fun getTransactions(@Header("Authorization") accessToken: String): Call<List<myTransaction>>

    @POST("transaction")
    fun createTransaction(@Header("Authorization") accessToken: String, @Body transaction: RequestBody): Call<String>

    @PUT("transaction/{id}")
    fun updateTransaction(@Header("Authorization") accessToken: String, @Body transaction: RequestBody, @Path("id") id: String): Call<String>

    @DELETE("transaction/{id}")
    fun deleteTransaction(@Header("Authorization") accessToken: String, @Path("id") id: String): Call<String>

    ////////////////////
    // Categories
    @GET("category/{id}")
    fun getCategory(@Header("Authorization") accessToken: String, @Path("id") id: Int): Call<CategoryTransaction>

    @GET("category")
    fun getCategories(@Header("Authorization") accessToken: String, @Path("id") id: Int): Call<List<CategoryTransaction>>

    @POST("category")
    fun createCategory(@Header("Authorization") accessToken: String, @Body categoryTransaction: RequestBody): Call<String>

    @PUT("category/{id}")
    fun updateCategory(@Header("Authorization") accessToken: String, @Body transaction: RequestBody, @Path("id") id: String)

    @DELETE("category/{id}")
    fun deleteCategory(@Header("Authorization") accessToken: String, @Path("id") id: String): Call<String>

    /////////////////////
    // Authorization
    @POST("api/v1/signUp")
    fun signUp(@Body credentials: RequestBody): Call<authTokens>


    @POST("api/v1/signIn")
    fun signIn(@Body credentials: RequestBody): Call<JSONObject>

    @POST("api/v1/signIn")
    fun reSignIn(@Body refreshToken: RequestBody): Call<authTokens>
}
