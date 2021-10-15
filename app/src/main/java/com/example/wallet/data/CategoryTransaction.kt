package com.example.wallet.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class CategoryTransaction(
    @SerializedName("id")
    @Expose
    var categoryId: UUID,
    @SerializedName("name")
    @Expose
    var nameOfCategory : String,
    @SerializedName("userId")
    @Expose
    var userId : UUID,
    @SerializedName("created")
    @Expose
    var dateOfCreate : String,
    @SerializedName("updated")
    @Expose
    var dateOfUpdate : String

)
