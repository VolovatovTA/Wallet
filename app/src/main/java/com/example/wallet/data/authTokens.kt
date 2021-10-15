package com.example.wallet.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class authTokens (
    @SerializedName("access")
    @Expose
    var accessToken: String,
    @SerializedName("refresh")
    @Expose
    var refreshToken: String
)
data class authTokensAndLog (
    @SerializedName("log")
    @Expose
    var log: String,
    @SerializedName("access")
    @Expose
    var accessToken: String,
    @SerializedName("refresh")
    @Expose
    var refreshToken: String
)
