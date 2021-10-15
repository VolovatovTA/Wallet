package com.example.wallet.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wallet.databinding.FragmentRegistrationBinding
import com.example.wallet.ui.registration.RegistrationViewModel


class RegistrationFragment : Fragment() {
    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        val personNameInRegistration = binding.editTextTextPersonName
        val personSecondNameInRegistration = binding.editTextTextPersonName2
        val personLoginInRegistration = binding.editTextTextEmailAddress
        val personPasswordInRegistration = binding.editTextTextPassword
        val personRepeatPasswordInRegistration = binding.editTextTextPassword2
        val signUpButton = binding.signUpButton

        registrationViewModel = ViewModelProvider(this, MyViewModelFactory())
            .get(RegistrationViewModel::class.java)
        registrationViewModel.registrationFormState.observe(viewLifecycleOwner, Observer {
            val registrationState = it ?: return@Observer
            signUpButton.isEnabled = registrationState.registrationForm_isDataValid

            if (registrationState.userFirstNameError != null){
                personNameInRegistration.error = getString(registrationState.userFirstNameError)
            }
            if (registrationState.emailError != null){
                personLoginInRegistration.error = getString(registrationState.emailError)
            }
            if (registrationState.passwordError != null){
                personPasswordInRegistration.error = getString(registrationState.passwordError)
            }
            if (registrationState.confirmPasswordError != null){
                personRepeatPasswordInRegistration.error = getString(registrationState.confirmPasswordError)
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
                registrationViewModel.registrationDataChanged(
                    personNameInRegistration.text.toString(),
                    personSecondNameInRegistration.text.toString(),
                    personLoginInRegistration.text.toString(),
                    personPasswordInRegistration.text.toString(),
                    personRepeatPasswordInRegistration.text.toString()
                )
            }

        }
        personLoginInRegistration.addTextChangedListener(afterTextChangedListener)
        personNameInRegistration.addTextChangedListener(afterTextChangedListener)
        personPasswordInRegistration.addTextChangedListener(afterTextChangedListener)
        personRepeatPasswordInRegistration.addTextChangedListener(afterTextChangedListener)
        personSecondNameInRegistration.addTextChangedListener(afterTextChangedListener)

        signUpButton.setOnClickListener {
            registrationViewModel.signUp(
                personNameInRegistration.text.toString(),
                personSecondNameInRegistration.text.toString(),
                personLoginInRegistration.text.toString(),
                personPasswordInRegistration.text.toString(),
                personRepeatPasswordInRegistration.text.toString()
            )
        }
        return binding.root
    }


}