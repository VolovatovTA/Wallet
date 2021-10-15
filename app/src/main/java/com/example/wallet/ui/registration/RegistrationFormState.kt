package com.example.wallet.ui.registration

class RegistrationFormState (
    val registrationForm_isDataValid: Boolean = false,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val userFirstNameError: Int? = null,
    val userSecondNameError: Int? = null,
    val emailError: Int? = null
)
