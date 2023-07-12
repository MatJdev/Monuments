package com.example.practica5.local.monuments

import com.example.practica5.local.monuments.dbo.ImageDBO
import com.example.practica5.local.monuments.dbo.LocationDBO
import com.example.practica5.local.monuments.dbo.MonumentDBO
import com.example.practica5.model.bo.image.ImageBO
import com.example.practica5.model.bo.location.LocationBO
import com.example.practica5.model.bo.monument.MonumentBO

internal fun MonumentDBO.toBO() = MonumentBO(
    id,
    name,
    city,
    description,
    images.map { it.toBO() },
    location.toBO(),
    urlExtraInformation,
    country,
    countryCode,
    isFromMyMonuments,
    countryFlag,
    isFavorite
)

internal fun ImageDBO.toBO() = ImageBO(id, url)

internal fun LocationDBO.toBO() = LocationBO(latitude, longitude)

internal fun MonumentBO.toDBO() = MonumentDBO(
    id,
    name,
    city,
    description,
    images.map { it.toDBO() },
    location.toDBO(),
    urlExtraInformation,
    country,
    countryCode,
    isFromMyMonuments,
    countryFlag,
    isFavorite
)

internal fun ImageBO.toDBO() = ImageDBO(id, url)

internal fun LocationBO.toDBO() = LocationDBO(latitude, longitude)
