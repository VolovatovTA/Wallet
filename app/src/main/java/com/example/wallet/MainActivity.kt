package com.example.wallet

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.wallet.data.placeholder.PlaceholderContent
import com.example.wallet.databinding.ActivityMainBinding
import com.example.wallet.ui.login.LoginFragment
import com.example.wallet.ui.login.LoginViewModel
import com.example.wallet.ui.transactions.TransactionsViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var isSelectionMode = false
    private val TAG = "Timofey"

//    public lateinit var navigationView: BottomNavigationView


    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.nav_view)
            .setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.blankForCreateTransactionFragment) {
                binding.navView.visibility = View.GONE
            } else {
                binding.navView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        if(isSelectionMode){
            inflater.inflate(R.menu.menu_edit_delete, menu)
        }

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, "item Selected", Toast.LENGTH_SHORT).show()

        if (item == findViewById(R.id.action_signOut)) {
//            AuthUI.getInstance().signOut(this)

        }
        if (item.itemId == R.id.app_bar_delete){
            TransactionsViewModel.deleteTransaction()
        }
        else if (item.itemId == R.id.app_bar_edit){
            Log.d(TAG, "edit1")        }

        return super.onOptionsItemSelected(item)
    }



    override fun onResume() {
        super.onResume()


    }
}