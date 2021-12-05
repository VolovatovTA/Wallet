package com.example.wallet.data

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.example.wallet.data.model.LoggedInUser


object Repository {
    val dataSource = DataSource()
    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        Log.d("Timofey", "login in rep")

        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    suspend fun signUp(
        userFirstName: String,
        userSecondName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<RegistratedInUser> {

        return dataSource.signUp(userFirstName, userSecondName, email, password, confirmPassword)
    }
    fun loginWithRefreshToken(activity: FragmentActivity?) {
        val settings = activity?.getSharedPreferences("mySettings", Context.MODE_PRIVATE)
        val refreshTokenFromStorage = settings?.getString("refresh token", "")

        Log.d("Timofey", "refreshTokenFromStorage = $refreshTokenFromStorage")

    }

}