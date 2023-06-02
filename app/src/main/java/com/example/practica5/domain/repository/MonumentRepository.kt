package com.example.practica5.domain.repository

import com.example.practica5.domain.model.bo.MonumentBO

interface MonumentRepository {
    suspend fun getMonuments(): List<MonumentBO>
    suspend fun updateMonument(id: Long, favorite: Boolean)
    suspend fun getMonumentsOrderedByNtoS(): List<MonumentBO>
    suspend fun getMonumentsOrderedByEtoW(): List<MonumentBO>
    suspend fun getSortedMonuments(sortMode: String): List<MonumentBO>
    suspend fun getUniqueCountries(): List<String>
    suspend fun getFilteredMonuments(country: String): List<MonumentBO>
}