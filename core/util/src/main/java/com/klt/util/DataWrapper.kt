package com.klt.util

import retrofit2.Response

sealed class Result<out T>(
    open val data: T? = null,
) {
    data class Success<out T>(override val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
}

inline fun <T> safeApiCall(
    apiCall: () -> Response<T>
): Result<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body() ?: return Result.Error(error = "EMPTY BODY")
            return Result.Success(data = body)
        }

        return Result.Error(
            error = "ERROR CODE ${response.code()} : ${response.errorBody().toString()}"
        )
    } catch (e: Exception) {
        return Result.Error(error = e.localizedMessage ?: "Something's Wrong!")
    }
}

inline fun <T, K> safeApiCall(
    successCall: () -> Response<T>,
    errorCall: () -> Response<K>
): Result<Any> {
    try {
        val response = successCall()
        if (response.isSuccessful) {
            val body = response.body() ?: return Result.Error(error = "EMPTY BODY")
            return Result.Success(data = body)
        }

        return Result.Error(
            error = "ERROR CODE ${response.code()} : ${response.errorBody().toString()}"
        )
    } catch (e: Exception) {
        try {
            val error = errorCall()
            if (error.isSuccessful) {
                val body = error.body() ?: return Result.Error(error = "EMPTY BODY")
                return Result.Success(data = body)
            }
            return Result.Error(
                error = "ERROR CODE ${error.code()} : ${error.errorBody().toString()}"
            )
        } catch (e: Exception) {
            return Result.Error(error = e.localizedMessage ?: "Something's Wrong!")
        }
    }
}