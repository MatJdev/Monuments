package com.example.practica5.data.model

import com.squareup.moshi.Json

data class LocationDTO(
    @Json(name = "latitudE")
    val latitude: Double?,
    val longitude: Double?
)
