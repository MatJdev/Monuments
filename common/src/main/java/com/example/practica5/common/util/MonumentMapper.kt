package com.example.practica5.common.util

import com.example.practica5.model.bo.image.ImageBO
import com.example.practica5.model.bo.location.LocationBO
import com.example.practica5.model.bo.monument.MonumentBO
import com.example.practica5.model.vo.ImageVO
import com.example.practica5.model.vo.LocationVO
import com.example.practica5.model.vo.MonumentVO

object MonumentMapper {

    fun mapMonumentBoToVo(monumentBO: MonumentBO): MonumentVO =
        MonumentVO(
            id = monumentBO.id,
            name = monumentBO.name,
            city = monumentBO.city,
            description = monumentBO.description,
            urlExtraInformation = monumentBO.urlExtraInformation,
            location = mapLocationBoToVo(monumentBO.location),
            images = monumentBO.images.map { mapImageBoToVo(it) },
            country = monumentBO.country,
            countryCode = monumentBO.countryCode,
            isFromMyMonuments = monumentBO.isFromMyMonuments,
            countryFlag = monumentBO.countryFlag,
            isFavorite = monumentBO.isFavorite
        )

    private fun mapLocationBoToVo(locationBO: LocationBO): LocationVO =
        LocationVO(
            latitude = locationBO.latitude,
            longitude = locationBO.longitude
        )

    private fun mapImageBoToVo(imageBO: ImageBO): ImageVO =
        ImageVO(
            id = imageBO.id,
            url = imageBO.url
        )

    fun mapMonumentVoToBo(monumentVO: MonumentVO): MonumentBO =
        MonumentBO(
            id = monumentVO.id,
            name = monumentVO.name,
            city = monumentVO.city,
            description = monumentVO.description,
            urlExtraInformation = monumentVO.urlExtraInformation,
            location = mapLocationVoToBo(monumentVO.location),
            images = monumentVO.images.map { mapImageVoToBo(it) },
            country = monumentVO.country,
            countryCode = monumentVO.countryCode,
            isFromMyMonuments = monumentVO.isFromMyMonuments,
            countryFlag = monumentVO.countryFlag,
            isFavorite = monumentVO.isFavorite
        )

    private fun mapLocationVoToBo(locationVO: LocationVO): LocationBO =
        LocationBO(
            latitude = locationVO.latitude,
            longitude = locationVO.longitude
        )

    private fun mapImageVoToBo(imageVO: ImageVO): ImageBO =
        ImageBO(
            id = imageVO.id,
            url = imageVO.url
        )
}