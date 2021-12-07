package com.example.wallet.data

import android.icu.util.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient


object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        val connTimeout = 10.toLong()
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(connTimeout, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(connTimeout, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(connTimeout, java.util.concurrent.TimeUnit.SECONDS)

        if (retrofit == null) {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build()
        }
        return retrofit!!
    }
}
