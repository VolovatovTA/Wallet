package com.example.wallet.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wallet.data.LoginDataSource
import com.example.wallet.data.LoginRepository
import com.example.wallet.ui.registration.RegistrationViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class MyViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource()
                )
            ) as T
        }
        else if(modelClass.isAssignableFrom(RegistrationViewModel::class.java)){
            return RegistrationViewModel(loginRepository = LoginRepository(dataSource = LoginDataSource())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}