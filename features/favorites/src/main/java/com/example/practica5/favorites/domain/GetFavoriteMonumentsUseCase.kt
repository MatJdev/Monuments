package com.example.practica5.favorites.domain

import com.example.practica5.data.repository.monuments.MonumentRepository
import com.example.practica5.model.bo.monument.MonumentBO
import javax.inject.Inject

class GetFavoriteMonumentsUseCase @Inject constructor(private val monumentRepository: MonumentRepository) {
    suspend operator fun invoke(): List<MonumentBO>? = monumentRepository.getFavoriteMonuments()
}