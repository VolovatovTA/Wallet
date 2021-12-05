package com.example.wallet.data

import android.os.Build
import android.util.Log
import com.example.wallet.R
import com.example.wallet.data.model.LoggedInUser
import com.example.wallet.data.model.MyCategory
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.*
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class DataSource {
    private val CODE_INTERNAL_SERVER_ERROR = 500
    val CODE_NOT_CORRECT_PASSWORD_OR_EMAIL = 422
    val CODE_EVERYTHING_IS_GOOD = 200


    private var retrofitService: JSonPlaceHolderAPI = NetworkService.jSonPlaceHolderAPI
    private var accessToken: String? = null
    private var refreshToken: String? = null



    suspend fun refreshAuth(refreshToken: String): String? {


        val response = retrofitService.reSignIn(createJsonRequestBody(
            "X-Auth-Token" to refreshToken)).awaitResponse()
//        val user = getDecodedJWT(accessToken!!)

//        if(response.message().)
        return response.body()?.refreshToken

    }

    private val TAG_Tim = "Timofey"
    suspend fun login(email: String, password: String): Result<LoggedInUser> {

        val response = retrofitService.signIn(
                createJsonRequestBody(
                    "email" to email, "password" to password
                )
         ).awaitResponse()
        Log.d(TAG_Tim, "Response.code = ${response.code()}")


        return when {
            response.code() == CODE_EVERYTHING_IS_GOOD -> {
                accessToken = response.body()!!.accessToken
                refreshToken = response.body()!!.refreshToken
                Result.Success(LoggedInUser( displayName = " name_from_JWT_mast_be decoded"))
            }
            response.code() == CODE_NOT_CORRECT_PASSWORD_OR_EMAIL -> {
                Result.Error(LoggedInUser(displayMessage = R.string.login_failed_by_password_or_email), IOException("password or email is not correct"))
            }
            response.code() == CODE_INTERNAL_SERVER_ERROR -> {
                Result.Error(LoggedInUser(displayMessage =  R.string.server_crushed), IOException("Internal server error"))
            }
            else -> {
                Result.Another(LoggedInUser(displayMessage = R.string.smth_wrong))
            }
        }

    }



    fun logout() {
        // TODO: revoke authentication
    }


    suspend fun signUp(userFirstName: String, userSecondName: String, email: String, password: String, confirmPassword: String): Result<RegistratedInUser> {


        retrofitService.signUp(createJsonRequestBody(
            "name" to userFirstName, "email" to email, "password" to password)).awaitResponse()
        val newUser = RegistratedInUser(userFirstName, userSecondName)
        return Result.Success(newUser)
    }


    class Json4Kotlin_Base {
        lateinit var user_id: String
        lateinit var id: String
        lateinit var exp: String

    }
    lateinit var transactions: List<myTransaction>
    lateinit var transaction: myTransaction
    lateinit var categories: List<MyCategory>




    private fun getDecodedJWT(jwt: String?): String {

        var result: String? = ""

        if (jwt == "Not Found") return ""
        else {
            val parts: List<String> = jwt?.split('.') ?: emptyList()
            Log.d("Timofey", "" + parts.toString())

            try {
                var index = 0
                for (part in parts) {
                    if (index >= 3) break
                    index++
                    val partAsBytes = part.toByteArray(charset("UTF-8"))
                    val decodedPart =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            String(
                                java.util.Base64.getUrlDecoder().decode(partAsBytes),
                                charset("UTF-8")
                            )
                        } else {
                            ""
                        }
                    result = if(index == 2) {decodedPart} else{""}

                    Log.d("Timofey", "result = $result")
                }
            } catch (e: Exception) {
                throw RuntimeException("Couldn't decode jwt", e)
            }
        }

        return result!!
    }

    private fun createJsonRequestBody(vararg params: Pair<String, Any>): RequestBody =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString()
        )





    suspend fun getTransactions() {
        val response = retrofitService.getTransactions(accessToken!!).awaitResponse()
        Log.d(TAG_Tim, "response.message = ${response.message()}")
        Log.d(TAG_Tim, "response.body = ${response.body()}")
        transactions = response.body()!!
    }

//    fun getTransaction(idTransaction: UUID, accessToken: String) {
//        retrofitService.getTransaction(accessToken, idTransaction.toString()).enqueue(callbackTransaction)
//
//    }

    suspend fun createTransaction(
        comment: String,
        amount: Float,
        currency: String,
        category: String,
         type: String
    ) : Result<CreateTransactionInUser> {
        Log.d(TAG_Tim, "type = $type")

        val response = retrofitService.createTransaction(
            accessToken!!,
            createJsonRequestBody(
                "comment" to comment,
                "amount" to amount,
                "currency" to currency,
                "category" to category,
                "type" to type)).awaitResponse()

        return if (response.code() == CODE_EVERYTHING_IS_GOOD){
            Result.Success(CreateTransactionInUser("OK", response.body()!!))
        } else{
            Result.Error(CreateTransactionInUser("smth went wrong"), IOException(""))
        }

    }

    fun updateTransaction(
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
                "dateOfUpdate" to dateOfUpdate))
    }

    fun deleteTransaction(id: UUID, accessToken: String) {

    }
}

data class CreateTransactionInUser (
val inform :String = "",
val transaction: myTransaction? = null
)
