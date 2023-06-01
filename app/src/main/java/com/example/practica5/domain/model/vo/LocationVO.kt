package com.example.practica5.domain.model.vo

import java.io.Serializable

data class LocationVO(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Serializable
