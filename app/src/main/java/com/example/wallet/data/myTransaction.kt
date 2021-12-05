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
    @SerializedName("categoryId")
    @Expose
    var categoryId : UUID?,
    @SerializedName("comment")
    @Expose
    var commentTransaction : String,
    @SerializedName("amount")
    @Expose
    var amount: Float,
    @SerializedName("currency")
    @Expose
    var currency : String,
    @SerializedName("type")
    @Expose
    var type : String,
    @SerializedName("updated")
    @Expose
    var dateOfUpdate : Date,
    @SerializedName("created")
    @Expose
    var dateOfCreate : Date

)