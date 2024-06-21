package com.main_screen.domain

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val error: Throwable) : Result<T>()
}

inline fun <T, R> Result<T>.processResult(
    success: (Result.Success<T>) -> R,
    error: (Result.Error<T>) -> R
): R = when (this) {
    is Result.Success -> success(this)
    is Result.Error -> error(this)
}