package com.example.searchmovielocalcache.utils

// A generic class that contains data and status about loading this data.
open class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val status: Status
) {
    class Success<T>(data: T) : Resource<T>(data, null, Status.SUCCESS)
    class Loading<T>(data: T? = null) : Resource<T>(data, null, Status.LOADING)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message, Status.ERROR)

    enum class Status{
        SUCCESS, ERROR, LOADING
    }
}