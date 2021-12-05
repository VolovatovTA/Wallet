package com.example.wallet.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error<out T : Any>(val data: T, val exception: Exception) : Result<T>()
    data class Another<out T : Any>(val data: T) : Result<T>() {

    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Another -> "Internal server error"
        }
    }
}