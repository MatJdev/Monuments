package com.example.practica5.domain.model.vo

import java.io.Serializable

data class MonumentVO(
    val id: Long = 0,
    val name: String = "",
    val city: String = "",
    val description: String = "",
    val images: List<ImageVO> = emptyList(),
    val location: LocationVO = LocationVO(),
    val urlExtraInformation: String = "",
    val country: String = "",
    val countryCode: String = "",
    val isFromMyMonuments: Boolean = false,
    val countryFlag: String = "",
    val isFavorite: Boolean = false
) : Serializable
