package com.example.wallet.data.model

import com.example.wallet.data.JSonPlaceHolderAPI
import com.example.wallet.data.NetworkService
import com.example.wallet.data.myTransaction
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*

class DataSource {
    private var retrofitService: JSonPlaceHolderAPI = NetworkService.jSonPlaceHolderAPI
    lateinit var transactions: List<myTransaction>
    lateinit var transaction: myTransaction


    private val callbackTransactions: retrofit2.Callback<List<myTransaction>> =
        object : retrofit2.Callback<List<myTransaction>> {
            override fun onResponse(
                call: Call<List<myTransaction>>,
                response: Response<List<myTransaction>>
            ) {
                transactions = response.body()!!

            }

            override fun onFailure(call: Call<List<myTransaction>>, t: Throwable) {
                transactions = emptyList()
            }

        }
    private val callbackTransaction: retrofit2.Callback<myTransaction> =
        object : retrofit2.Callback<myTransaction> {
            override fun onResponse(
                call: Call<myTransaction>,
                response: Response<myTransaction>
            ) {
                transaction = response.body()!!

            }

            override fun onFailure(call: Call<myTransaction>, t: Throwable) {
                transactions = emptyList()
            }

        }
    private fun createJsonRequestBody(vararg params: Pair<String, String>): RequestBody =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString()
        )

    private val callbackCreate: retrofit2.Callback<String> = object: retrofit2.Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
        }

    }
    private val callbackUpdate: retrofit2.Callback<String> = object: retrofit2.Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) {
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
        }

    }
    fun getTransactions(userId: UUID, accessToken: String) {
        retrofitService.getTransactions(accessToken).enqueue(callbackTransactions)
    }

    fun getTransaction(idTransaction: UUID, accessToken: String) {
        retrofitService.getTransaction(accessToken, idTransaction.toString()).enqueue(callbackTransaction)

    }

    fun createTransaction(
        userId: UUID,
        comment: String,
        amount: Float,
        currency: String,
        dateOfUpdate: String,
        dateOfCreate: String,
        accessToken: String
    ) {

        retrofitService.createTransaction(
            accessToken,
            createJsonRequestBody(
                "userId" to userId.toString(),
                "comment" to comment,
                "amount" to amount.toString(),
                "currency" to currency,
                "dateOfCreate" to dateOfCreate,
                "dateOfUpdate" to dateOfUpdate)).enqueue(callbackCreate)

    }

    fun updateTransaction(
        id: UUID,
        userId: UUID,
        comment: String,
        amount: Float,
        currency: String,
        dateOfUpdate: String,
        dateOfCreate: String,
        accessToken: String
    ) {
        retrofitService.createTransaction(
            accessToken,
            createJsonRequestBody(
                "userId" to userId.toString(),
                "comment" to comment,
                "amount" to amount.toString(),
                "currency" to currency,
                "dateOfCreate" to dateOfCreate,
                "dateOfUpdate" to dateOfUpdate)).enqueue(callbackUpdate)
    }

    fun deleteTransaction(id: UUID, accessToken: String) {

    }
}