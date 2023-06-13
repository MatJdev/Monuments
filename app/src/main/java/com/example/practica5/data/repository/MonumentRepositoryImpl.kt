package com.example.practica5.data.repository

import com.example.practica5.core.RetrofitHelper
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.domain.model.bo.MonumentBO
import com.example.practica5.domain.repository.LocalMonumentDataSource
import com.example.practica5.domain.repository.MonumentRepository
import com.example.practica5.domain.repository.RemoteMonumentDataSource
import com.example.practica5.utils.MonumentsConstant.EMPTY_INFO

class MonumentRepositoryImpl(
    private val localDataSource: LocalMonumentDataSource,
    private val remoteDataSource: RemoteMonumentDataSource
) : MonumentRepository {

    override suspend fun getMonumentsFromLocal(): List<MonumentBO> {
        return localDataSource.getAllMonuments()
    }

    override suspend fun getMonumentsFromRemote(): List<MonumentBO> {
        return remoteDataSource.getMonuments()
    }

    override suspend fun insertMonument(monument: List<MonumentBO>) {
        localDataSource.insertMonument(monument)
    }

    override suspend fun updateMonument(id: Long, favorite: Boolean) {
        localDataSource.updateFavoriteMonument(id, favorite)
    }

    override suspend fun insertOneMonument(monument: MonumentBO) {
        localDataSource.insertOneMonument(MonumentMapper.mapMonumentBoToDbo(monument))
    }

    override suspend fun getMyMonuments(): List<MonumentBO> {
        return localDataSource.getMyMonuments()
    }

    override suspend fun deleteMonument(monument: MonumentBO) {
        localDataSource.deleteMonument(MonumentMapper.mapMonumentBoToDbo(monument))
    }

    override suspend fun getFavoriteMonuments(): List<MonumentBO> {
        return localDataSource.getFavoriteMonuments()
    }

    override suspend fun getMonumentsOrderedByNtoS(): List<MonumentBO> {
        return localDataSource.getMonumentsOrderByLocationNtoS()
    }

    override suspend fun getMonumentsOrderedByEtoW(): List<MonumentBO> {
        return localDataSource.getMonumentsOrderByLocationEtoW()
    }

    override suspend fun getSortedMonuments(sortMode: String): List<MonumentBO> {
        return localDataSource.getSortedMonuments(sortMode)
    }

    override suspend fun getUniqueCountries(): List<String> {
        return localDataSource.getUniqueCountries()
    }

    override suspend fun getFilteredMonuments(country: String): List<MonumentBO> {
        return localDataSource.getFilteredMonuments(country)
    }

    override suspend fun getCountryFlag(countryCode: String): String {
        val response = RetrofitHelper.flagsApiServiceInstance.getFlagImage(countryCode)
        return if (response.isSuccessful) {
            response.raw().request.url.toString()
        } else {
            EMPTY_INFO
        }
    }
}