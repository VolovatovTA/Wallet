package com.example.wallet.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AuthTokens (
    @SerializedName("access")
    @Expose
    var accessToken: String,
    @SerializedName("refresh")
    @Expose
    var refreshToken: String
)
