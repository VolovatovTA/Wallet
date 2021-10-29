package com.example.wallet.data

object NetworkService {
    private const val BASE_URL = "https://bysoft.ru/wallet/api/v1/"
    val jSonPlaceHolderAPI: JSonPlaceHolderAPI
        get() = RetrofitClient.getClient(BASE_URL).create(JSonPlaceHolderAPI::class.java)
}
