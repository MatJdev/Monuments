package com.example.practica5.datasource.monuments

import com.example.practica5.model.bo.monument.MonumentBO

interface LocalMonumentDataSource {
    suspend fun getAllMonuments(): List<MonumentBO>
    suspend fun insertMonument(monument: List<MonumentBO>)
    suspend fun updateFavoriteMonument(id: Long, favorite: Boolean)
    suspend fun getMonumentsOrderByLocationNtoS(): List<MonumentBO>
    suspend fun getMonumentsOrderByLocationEtoW(): List<MonumentBO>
    suspend fun getSortedMonuments(sortMode: String): List<MonumentBO>
    suspend fun getUniqueCountries(): List<String>
    suspend fun getFilteredMonuments(country: String): List<MonumentBO>
    suspend fun insertOneMonument(monument: MonumentBO) //DBO
    suspend fun getMyMonuments(): List<MonumentBO>
    suspend fun deleteMonument(monument: MonumentBO) //DBO
    suspend fun getFavoriteMonuments(): List<MonumentBO>
}