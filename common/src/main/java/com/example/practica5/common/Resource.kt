package com.example.practica5.common

sealed class Resource<out DATA, out ERROR> {
    object Loading : Resource<Nothing, Nothing>()
    data class Success<out DATA>(val data: DATA) : Resource<DATA, Nothing>()
    data class Error<out ERROR>(val error: ERROR) : Resource<Nothing, ERROR>()
}
