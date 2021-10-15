package com.example.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wallet.databinding.ActivityMainBinding
import com.example.wallet.ui.login.LoginFragment
import com.example.wallet.ui.login.RegistrationFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.container, LoginFragment()).commit()
        Log.d("Timofey", supportFragmentManager.fragments.toString())

    }
}