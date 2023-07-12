package com.example.practica5.data.remote.monuments.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MonumentDTO(
    val id: Long?,
    val name: String?,
    @Json(name = "citY")
    val city: String?,
    val description: String?,
    val images: List<ImageDTO?>?,
    val location: LocationDTO?,
    val urlExtraInformation: String?,
    val country: String?,
    val countryCode: String?,
    val isFromMyMonuments: Boolean?,
    val countryFlag: String?,
    val isFavorite: Boolean?
)
