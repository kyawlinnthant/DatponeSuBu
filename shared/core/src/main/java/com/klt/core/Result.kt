package com.klt.core

sealed class Result<out T>(
    open val data : T? = null
){
    data class Success<T>(override val data: T): Result<T>()
    data class Error(val error : String): Result<Nothing>()
}

