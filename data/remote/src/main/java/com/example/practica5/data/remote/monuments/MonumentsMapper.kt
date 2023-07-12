package com.example.practica5.data.remote.monuments

import com.example.practica5.data.remote.monuments.dto.ImageDTO
import com.example.practica5.data.remote.monuments.dto.LocationDTO
import com.example.practica5.data.remote.monuments.dto.MonumentDTO
import com.example.practica5.model.bo.image.ImageBO
import com.example.practica5.model.bo.location.LocationBO
import com.example.practica5.model.bo.monument.MonumentBO

internal fun MonumentDTO.toBO() = MonumentBO(
    id ?: 0,
    name ?: "",
    city ?: "",
    description ?: "",
    images?.mapNotNull { it?.toBO() } ?: emptyList(),
    location?.toBO() ?: LocationBO(0.0, 0.0),
    urlExtraInformation ?: "",
    country ?: "",
    countryCode ?: "",
    isFromMyMonuments ?: false,
    countryFlag ?: "",
    isFavorite ?: false
)

internal fun ImageDTO.toBO() = ImageBO(id ?: 0, url ?: "")

internal fun LocationDTO.toBO() = LocationBO(latitude ?: 0.0, longitude ?: 0.0)