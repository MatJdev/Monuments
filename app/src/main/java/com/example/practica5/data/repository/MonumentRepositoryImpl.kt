package com.example.practica5.data.repository

import android.content.Context
import com.example.practica5.core.RetrofitHelper
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.model.MonumentDTO
import com.example.practica5.data.model.MonumentProvider
import com.example.practica5.domain.model.bo.MonumentBO
import com.example.practica5.domain.repository.LocalMonumentDataSource
import com.example.practica5.domain.repository.MonumentRepository
import com.example.practica5.domain.repository.RemoteMonumentDataSource
import com.example.practica5.utils.MonumentsUtils.getCountryCodeFromLocation
import com.example.practica5.utils.MonumentsUtils.getCountryFromLocation

class MonumentRepositoryImpl(
    private val localDataSource: LocalMonumentDataSource,
    private val remoteDataSource: RemoteMonumentDataSource,
    private val context: Context
) : MonumentRepository {

    override suspend fun getMonuments(): List<MonumentBO> {
        val cachedMonuments = localDataSource.getAllMonuments()
        if (cachedMonuments.isNotEmpty()) {
            return cachedMonuments.map { MonumentMapper.mapMonumentDbotoBo(it) }
        }

        val response = remoteDataSource.getMonuments()
        val processData = setExtraProperties(response)

        MonumentProvider.monuments = processData
        localDataSource.insertMonument(processData.map { MonumentMapper.mapMonumentDtoToDbo(it) })
        return processData.map { MonumentMapper.mapMonumentDtoToBo(it) }
    }

    override suspend fun updateMonument(id: Long, favorite: Boolean) {
        localDataSource.updateFavoriteMonument(id, favorite)
    }

    private suspend fun getFlagUrlFromApi(countryCode: String): String {
        val response = RetrofitHelper.getFlagsApiService().getFlagImage(countryCode)
        return if (response.isSuccessful) {
            response.raw().request.url.toString()
        } else {
            ""
        }
    }

    private suspend fun setExtraProperties(response: List<MonumentDTO>): ArrayList<MonumentDTO> {
        val processData = ArrayList<MonumentDTO>()
        for (monument in response) {
            if ((monument.location?.latitude != null) && (monument.location.longitude != null)) {
                val country = getCountryFromLocation(context, monument.location.latitude, monument.location.longitude)
                val countryCode =
                    getCountryCodeFromLocation(context, monument.location.latitude, monument.location.longitude)
                if (countryCode != null) {
                    val countryFlag = getFlagUrlFromApi(countryCode)
                    val monumentWithCountry = monument.copy(
                        country = country,
                        countryCode = countryCode,
                        countryFlag = countryFlag
                    )
                    processData.add(monumentWithCountry)
                }
            }
        }

        return processData
    }

    override suspend fun getMonumentsOrderedByNtoS(): List<MonumentBO> {
        return localDataSource.getMonumentsOrderByLocationNtoS().map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun getMonumentsOrderedByEtoW(): List<MonumentBO> {
        return localDataSource.getMonumentsOrderByLocationEtoW().map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun getSortedMonuments(sortMode: String): List<MonumentBO> {
        return localDataSource.getSortedMonuments(sortMode).map { MonumentMapper.mapMonumentDbotoBo(it) }
    }

    override suspend fun getUniqueCountries(): List<String> {
        return localDataSource.getUniqueCountries()
    }

    override suspend fun getFilteredMonuments(country: String): List<MonumentBO> {
        return localDataSource.getFilteredMonuments(country).map { MonumentMapper.mapMonumentDbotoBo(it) }
    }
}