package com.example.wallet.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val displayName: String = "",
    val displayMessage: Int = 0
)