package com.example.practica5.domain

interface LocationHelper {
    suspend fun getCountryFromLocation(latitude: Double, longitude: Double): String
    suspend fun getCountryCodeFromLocation(latitude: Double, longitude: Double): String?
}