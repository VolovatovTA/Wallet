package com.example.wallet.ui.login

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

import com.example.wallet.R
import com.example.wallet.data.*
import com.example.wallet.data.model.LoggedInUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val TAG_Tim = "Timofey"

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
    CoroutineScope(Main).launch {
        when (val result = repository.dataSource.login(email, password)) {
            is Result.Success -> {
                _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            }
            is Result.Error -> {
                if (result.data.displayMessage != 0)
                _loginResult.value = LoginResult(error = result.data.displayMessage)
            }
            is Result.Another -> {
                _loginResult.value = LoginResult(error = R.string.something_went_wrong)
            }
        }
    }



    }




//    fun saveRefreshToken(activity: FragmentActivity?) {
//        val APP_PREFERENCES = "mySettingsAndTokens"
//
//
//        if (activity != null){
//
//            val sp = activity.applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
//            sp.edit().putString("access token", accessToken).apply()
//            sp.edit().putString("refresh token", refreshToken).apply()
//        }
//
//    }
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@") && username.isNotBlank()) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }




}