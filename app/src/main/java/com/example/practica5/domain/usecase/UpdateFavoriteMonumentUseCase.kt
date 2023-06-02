package com.example.practica5.domain.usecase

import com.example.practica5.domain.repository.MonumentRepository

class UpdateFavoriteMonumentUseCase(private val monumentRepository: MonumentRepository, private val monumentId: Long, private val favorite: Boolean) {
    suspend operator fun invoke() = monumentRepository.updateMonument(id = monumentId, favorite = favorite)
}