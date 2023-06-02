package com.example.practica5.data.mapper

import com.example.practica5.data.database.entities.ImageDBO
import com.example.practica5.data.database.entities.LocationDBO
import com.example.practica5.data.database.entities.MonumentDBO
import com.example.practica5.data.model.ImageDTO
import com.example.practica5.data.model.LocationDTO
import com.example.practica5.data.model.MonumentDTO
import com.example.practica5.domain.model.bo.ImageBO
import com.example.practica5.domain.model.bo.LocationBO
import com.example.practica5.domain.model.bo.MonumentBO
import com.example.practica5.domain.model.vo.ImageVO
import com.example.practica5.domain.model.vo.LocationVO
import com.example.practica5.domain.model.vo.MonumentVO

object MonumentMapper {
    fun mapMonumentDtoToDbo(monumentDTO: MonumentDTO): MonumentDBO =
        MonumentDBO(
            id = monumentDTO.id ?: 0,
            name = monumentDTO.name ?: "",
            city = monumentDTO.city ?: "",
            description = monumentDTO.description ?: "",
            urlExtraInformation = monumentDTO.urlExtraInformation ?: "",
            location = mapLocationDtoToDbo(monumentDTO.location),
            images = monumentDTO.images?.mapNotNull { mapImageDtoToDbo(it) } ?: emptyList(),
            country = monumentDTO.country ?: "",
            countryCode = monumentDTO.countryCode ?: "",
            isFromMyMonuments = monumentDTO.isFromMyMonuments ?: false,
            countryFlag = monumentDTO.countryFlag ?: "",
            isFavorite = monumentDTO.isFavorite ?: false
        )


    private fun mapLocationDtoToDbo(locationDTO: LocationDTO?): LocationDBO =
        LocationDBO(
            latitude = locationDTO?.latitude ?: 0.0,
            longitude = locationDTO?.longitude ?: 0.0
        )

    private fun mapImageDtoToDbo(imageDTO: ImageDTO?): ImageDBO =
        ImageDBO(
            id = imageDTO?.id ?: 0,
            url = imageDTO?.url ?: ""
        )

    fun mapMonumentDtoToBo(monumentDTO: MonumentDTO): MonumentBO =
        MonumentBO(
            id = monumentDTO.id ?: 0,
            name = monumentDTO.name ?: "",
            city = monumentDTO.city ?: "",
            description = monumentDTO.description ?: "",
            urlExtraInformation = monumentDTO.urlExtraInformation ?: "",
            location = mapLocationDtoToBo(monumentDTO.location),
            images = monumentDTO.images?.mapNotNull { mapImageDtoToBo(it) } ?: emptyList(),
            country = monumentDTO.country ?: "",
            countryCode = monumentDTO.countryCode ?: "",
            isFromMyMonuments = monumentDTO.isFromMyMonuments ?: false,
            countryFlag = monumentDTO.countryFlag ?: "",
            isFavorite = monumentDTO.isFavorite ?: false
        )

    private fun mapLocationDtoToBo(locationDTO: LocationDTO?): LocationBO =
        LocationBO(
            latitude = locationDTO?.latitude ?: 0.0,
            longitude = locationDTO?.longitude ?: 0.0
        )

    private fun mapImageDtoToBo(imageDTO: ImageDTO?): ImageBO =
        ImageBO(
            id = imageDTO?.id ?: 0,
            url = imageDTO?.url ?: ""
        )

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

    fun mapMonumentDbotoBo(monumentDBO: MonumentDBO): MonumentBO =
        MonumentBO(
            id = monumentDBO.id,
            name = monumentDBO.name,
            city = monumentDBO.city,
            description = monumentDBO.description,
            urlExtraInformation = monumentDBO.urlExtraInformation,
            location = mapLocationDboToBo(monumentDBO.location),
            images = monumentDBO.images.map { mapImageDboToBo(it) },
            country = monumentDBO.country,
            countryCode = monumentDBO.countryCode,
            isFromMyMonuments = monumentDBO.isFromMyMonuments,
            countryFlag = monumentDBO.countryFlag,
            isFavorite = monumentDBO.isFavorite
        )

    private fun mapLocationDboToBo(locationDBO: LocationDBO?): LocationBO =
        LocationBO(
            latitude = locationDBO?.latitude ?: 0.0,
            longitude = locationDBO?.longitude ?: 0.0
        )

    private fun mapImageDboToBo(imageDBO: ImageDBO?): ImageBO =
        ImageBO(
            id = imageDBO?.id ?: 0,
            url = imageDBO?.url ?: ""
        )

    fun mapMonumentBoToDbo(monumentBO: MonumentBO): MonumentDBO =
        MonumentDBO(
            id = monumentBO.id,
            name = monumentBO.name,
            city = monumentBO.city,
            description = monumentBO.description,
            urlExtraInformation = monumentBO.urlExtraInformation,
            location = mapLocationBoToDbo(monumentBO.location),
            images = monumentBO.images.map { mapImageBoToDbo(it) },
            country = monumentBO.country,
            countryCode = monumentBO.countryCode,
            isFromMyMonuments = monumentBO.isFromMyMonuments,
            countryFlag = monumentBO.countryFlag,
            isFavorite = monumentBO.isFavorite
        )

    private fun mapLocationBoToDbo(locationBO: LocationBO): LocationDBO =
        LocationDBO(
            latitude = locationBO.latitude,
            longitude = locationBO.longitude
        )

    private fun mapImageBoToDbo(imageBO: ImageBO): ImageDBO =
        ImageDBO(
            id = imageBO.id,
            url = imageBO.url
        )
}