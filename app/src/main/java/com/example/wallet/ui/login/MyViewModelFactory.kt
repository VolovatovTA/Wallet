package com.example.wallet.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wallet.data.Repository
import java.io.IOException

class MyViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == LoginViewModel::class.java){
            return LoginViewModel(Repository) as T
        }
        else throw IOException("nothing class.java pointed")
    }

}
