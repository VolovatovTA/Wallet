package com.example.wallet.data

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.wallet.MainActivity
import com.example.wallet.data.model.LoggedInUser
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    val APP_PREFERENCES = "mySettingsAndTokens"

    private var retrofitService: JSonPlaceHolderAPI = NetworkService.jSonPlaceHolderAPI
    private var accessToken: String? = null

    fun reLogin(email: String, password: String): Result<LoggedInUser> {
        retrofitService.reSignIn(createJsonRequestBody(
            "email" to email, "password" to password)).enqueue(object: Callback<authTokens> {
            override fun onResponse(call: Call<authTokens>, response: Response<authTokens>) {
                Log.d("Timofey", "onResponse")
                Log.d("Timofey", "response.toString() = $response")
                Log.d("Timofey", "response.headers().toString() = " + response.headers().toString())

//                accessToken = response.body()?.accessToken
//                val sp = MainActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
//                val result = sp.edit().putString("access token", response.body()?.accessToken).apply()


            }

            override fun onFailure(call: Call<authTokens>, t: Throwable) {

            }
        })
        val user = getDecodedJWT(accessToken!!)

        return Result.Success(LoggedInUser(user, ""))

    }

            fun login(email: String, password: String): Result<LoggedInUser> {
        retrofitService.signIn(createJsonRequestBody(
            "email" to email, "password" to password)).enqueue(object: Callback<JSONObject> {
            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                Log.d("Timofey", "onResponse")
                Log.d("Timofey", "response.toString() = $response")
                Log.d("Timofey","response.headers().toString() = " + response.headers().toString())

//                accessToken = response.body()?.accessToken
//                val sp = MainActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
//                val result = sp.edit().putString("access token", response.body()?.accessToken).apply()


            }

            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                Log.d("Timofey", "onFailure")
                Log.d("Timofey", "call.request = ${call.request()}")
                Log.d("Timofey", t.stackTraceToString())
            }

        })
        try {


            val user = getDecodedJWT(accessToken!!)
            return Result.Success(LoggedInUser(user, "new name"))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    private fun createJsonRequestBody(vararg params: Pair<String, String>): RequestBody =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString())

    fun signUp(userFirstName: String, userSecondName: String, email: String, password: String, confirmPassword: String): Result<RegistratedInUser> {
        Log.d("Timofey", createJsonRequestBody("email" to email, "password" to password).contentType().toString())
        Log.d("Timofey", createJsonRequestBody("email" to email, "password" to password).contentLength().toString())
        Log.d("Timofey", JSONObject(mapOf("email" to email, "password" to password)).toString())

        retrofitService.signUp(createJsonRequestBody(
            "email" to email, "password" to password)).enqueue(object: retrofit2.Callback<authTokens> {
            override fun onResponse(call: Call<authTokens>, response: Response<authTokens>) {
                Log.d("Timofey", "onResponse")
                Log.d("Timofey", "response.toString() = $response")
                Log.d("Timofey","response.headers().toString() = " + response.headers().toString())

                accessToken = response.body()?.accessToken
//                val sp = MainActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
//                val result = sp.edit().putString("access token", response.body()?.accessToken).apply()


            }

            override fun onFailure(call: Call<authTokens>, t: Throwable) {
                Log.d("Timofey", "onFailure")
                Log.d("Timofey", call.toString())
                Log.d("Timofey", t.stackTraceToString())
            }

        })
        val newUser = RegistratedInUser(userFirstName, userSecondName)
        return Result.Success(newUser)
    }
    private fun getDecodedJWT(jwt: String): String {

        var result: String? = ""

        if (jwt == "Not Found") return ""
        else {
            val parts: List<String> = jwt.split("[.]")
            try {
                var index = 0
                for (part in parts) {
                    if (index >= 2) break
                    index++
                    val partAsBytes = part.toByteArray(charset("UTF-8"))
                    val decodedPart =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            String(
                                java.util.Base64.getUrlDecoder().decode(partAsBytes),
                                charset("UTF-8")
                            )
                        } else {
                            TODO("VERSION.SDK_INT < O")
                        }
                    result += decodedPart
                }
            } catch (e: Exception) {
                throw RuntimeException("Couldn't decode jwt", e)
            }
        }
        return result!!
    }
}