package com.example.wallet.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.wallet.databinding.FragmentLoginBinding

import com.example.wallet.R
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var loginViewModel: LoginViewModel
    var _binding: FragmentLoginBinding? = null
    private lateinit var navController: NavController

    companion object {
        const val TAG = "Timofey"
        const val SIGN_IN_RESULT_CODE = 1001
    }
//    private fun launchSignInFlow() {
//        Log.d(TAG, "launchSignInFlow")
//
//        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
//        )
//
//        startActivityForResult(
//            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
//                providers
//            ).build(), SIGN_IN_RESULT_CODE
//        )
//    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == SIGN_IN_RESULT_CODE) {
//            val response = IdpResponse.fromResultIntent(data)
//            if (resultCode == Activity.RESULT_OK) {
//                // Successfully signed in user.
//                i(
//                    TAG,
//                    "Successfully signed in user " +
//                            "${FirebaseAuth.getInstance().currentUser?.displayName}!"
//                )
//            } else {
//                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
//            }
//        }
//    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        bindingActivity = ActivityMainBinding.inflate(layoutInflater)
        Log.d(TAG, "LoginFragment onCreateView")


        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loginViewModel = ViewModelProvider(this, MyViewModelFactory())[LoginViewModel::class.java]

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading
        val signUpTextView = binding.registration
        val forgotPasswordTextView = binding.forgotPassword
        val signInWithGoogleButton = binding.btnSignInWithGoogle

            /*
            DELETE Before publication
             */
        binding.autoIn.setOnClickListener{
            usernameEditText.text = Editable.Factory.getInstance().newEditable("1@1.1")
            passwordEditText.text = Editable.Factory.getInstance().newEditable("111111")

        }

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
                loginFormState.isNetworkError?.let {
                    loadingProgressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "Sorry the connection is lost", Snackbar.LENGTH_LONG).show()
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
//                    loginViewModel.saveRefreshToken(activity)
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
//        signInWithGoogleButton.setOnClickListener { launchSignInFlow() }
        signUpTextView.setOnClickListener(this)
//        loginViewModel.loginWithRefreshToken(activity)
        navController = findNavController()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.popBackStack(R.id.loginFragment, false)
        }

//        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
//            when (authenticationState) {
//                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
//                    navController.popBackStack()
//                    navController.navigate(R.id.transactionItemFragment)
//                }
//                LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION -> Snackbar.make(
//                    view, requireActivity().getString(R.string.login_failed),
//                    Snackbar.LENGTH_LONG
//                ).show()
//                else -> Log.e(
//                    TAG,
//                    "Authentication state that doesn't require any UI change $authenticationState"
//                )
//            }
//        })
    }



    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        navController.navigate(R.id.transactionItemFragment)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
//        Log.d("Timofey", "onClick, p0.id = " + p0?.id + "; R.id.registration = " + R.id.registration)
        if (p0?.id == R.id.registration){
            navController.navigate(R.id.registrationFragment)
        }
    }
}