package com.example.practica5.model.bo.monument

import com.example.practica5.model.bo.image.ImageBO
import com.example.practica5.model.bo.location.LocationBO

data class MonumentBO(
    val id: Long,
    val name: String,
    val city: String,
    val description: String,
    val images: List<ImageBO>,
    val location: LocationBO,
    val urlExtraInformation: String,
    val country: String,
    val countryCode: String,
    val isFromMyMonuments: Boolean,
    val countryFlag: String,
    val isFavorite: Boolean
)