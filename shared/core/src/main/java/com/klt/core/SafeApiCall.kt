package com.klt.core

import retrofit2.Response

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

