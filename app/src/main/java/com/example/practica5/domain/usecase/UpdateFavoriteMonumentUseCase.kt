package com.example.practica5.domain.usecase

import com.example.practica5.domain.repository.MonumentRepository
import javax.inject.Inject

class UpdateFavoriteMonumentUseCase @Inject constructor(private val monumentRepository: MonumentRepository) {
    suspend operator fun invoke(monumentId: Long, favorite: Boolean) = monumentRepository.updateMonument(id = monumentId, favorite = favorite)
}