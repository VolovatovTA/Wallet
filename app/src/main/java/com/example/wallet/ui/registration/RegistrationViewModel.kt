package com.example.wallet.ui.registration

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wallet.R
import com.example.wallet.data.Repository
import com.example.wallet.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: Repository) : ViewModel(){
    private val _registrationForm = MutableLiveData<RegistrationFormState>()
    val registrationFormState: LiveData<RegistrationFormState> = _registrationForm

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    fun signUp(userFirstName: String, userSecondName: String, email: String, password: String, confirmPassword: String) {
        // can be launched in a separate asynchronous job
        CoroutineScope(Main).launch {
            val result = repository.signUp(userFirstName, userSecondName, email, password, confirmPassword)

            if (result is Result.Success) {
                _registrationResult.value =
                    RegistrationResult(success = RegistratedInUserView(displayName = result.data.firstName + " " + result.data.secondName))
            } else {
                _registrationResult.value = RegistrationResult(error = R.string.login_failed_by_password_or_email)
            }
        }

    }

    fun registrationDataChanged(
        userFirstName: String,
        userSecondName: String,
        email: String,
        password: String,
        confirmPassword: String) {
        if (!isEmailValid(email)) {
            _registrationForm.value = RegistrationFormState(emailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registrationForm.value = RegistrationFormState(passwordError = R.string.invalid_password)
        } else {
            _registrationForm.value = RegistrationFormState(registrationForm_isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isEmailValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}


