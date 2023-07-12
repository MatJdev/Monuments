package com.example.practica5.commonfeatures.domain

import com.example.practica5.common.domain.LocationHelper
import com.example.practica5.data.repository.monuments.MonumentRepository
import com.example.practica5.model.bo.monument.MonumentBO
import javax.inject.Inject

class GetMonumentsUseCase @Inject constructor(
    private val monumentRepository: MonumentRepository,
    private val locationHelper: LocationHelper
) {

    suspend fun getMonuments(): List<MonumentBO> {
        val cachedMonuments = monumentRepository.getMonumentsFromLocal()
        return cachedMonuments.ifEmpty {
            val remoteMonuments = monumentRepository.getMonumentsFromRemote()
            val processedData = setExtraProperties(remoteMonuments)
            monumentRepository.insertMonument(processedData)
            processedData
        }
    }

    private suspend fun setExtraProperties(response: List<MonumentBO>): List<MonumentBO> {
        val processedData = mutableListOf<MonumentBO>()
        for (monument in response) {
            val country = locationHelper.getCountryFromLocation(monument.location.latitude, monument.location.longitude)
            val countryCode = locationHelper.getCountryCodeFromLocation(monument.location.latitude, monument.location.longitude)
            if (countryCode != null) {
                val countryFlag = monumentRepository.getCountryFlag(countryCode)
                val monumentWithCountry = monument.copy(
                    country = country,
                    countryCode = countryCode,
                    countryFlag = countryFlag
                )
                processedData.add(monumentWithCountry)
            }
        }
        return processedData
    }
}