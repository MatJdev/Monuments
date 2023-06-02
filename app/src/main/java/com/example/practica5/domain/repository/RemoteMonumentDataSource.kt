package com.example.practica5.domain.repository

import com.example.practica5.data.model.MonumentDTO

interface RemoteMonumentDataSource {
    suspend fun getMonuments(): List<MonumentDTO>
}