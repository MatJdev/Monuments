package com.example.practica5.data.repository.monuments

import com.example.practica5.model.bo.monument.MonumentBO

interface MonumentRepository {
    suspend fun getMonumentsFromLocal(): List<MonumentBO>
    suspend fun getMonumentsFromRemote(): List<MonumentBO>
    suspend fun insertMonument(monument: List<MonumentBO>)
    suspend fun updateMonument(id: Long, favorite: Boolean)
    suspend fun getMonumentsOrderedByNtoS(): List<MonumentBO>
    suspend fun getMonumentsOrderedByEtoW(): List<MonumentBO>
    suspend fun getSortedMonuments(sortMode: String): List<MonumentBO>
    suspend fun getUniqueCountries(): List<String>
    suspend fun getFilteredMonuments(country: String): List<MonumentBO>
    suspend fun getCountryFlag(countryCode: String): String
    suspend fun insertOneMonument(monument: MonumentBO)
    suspend fun getMyMonuments(): List<MonumentBO>
    suspend fun deleteMonument(monument: MonumentBO)
    suspend fun getFavoriteMonuments(): List<MonumentBO>
}