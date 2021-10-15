package com.example.wallet.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class myTransaction (
    @SerializedName("id")
    @Expose
    var transactionId: UUID,
    @SerializedName("userId")
    @Expose
    var userId : UUID,
    @SerializedName("created")
    @Expose
    var dateOfCreate : String,
    @SerializedName("updated")
    @Expose
    var dateOfUpdate : String,
    @SerializedName("comment")
    @Expose
    var commentTransaction : String,
    @SerializedName("currency")
    @Expose
    var currency : String,
    @SerializedName("amount")
    @Expose
    var amount: Float

)