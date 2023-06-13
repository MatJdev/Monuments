package com.example.practica5.domain.repository

import com.example.practica5.domain.model.bo.MonumentBO

interface RemoteMonumentDataSource {
    suspend fun getMonuments(): List<MonumentBO>
}