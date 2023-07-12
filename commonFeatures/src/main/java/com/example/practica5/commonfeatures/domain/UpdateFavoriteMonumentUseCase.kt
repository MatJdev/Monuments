package com.example.practica5.commonfeatures.domain

import com.example.practica5.data.repository.monuments.MonumentRepository
import javax.inject.Inject

class UpdateFavoriteMonumentUseCase @Inject constructor(private val monumentRepository: MonumentRepository) {
    suspend operator fun invoke(monumentId: Long, favorite: Boolean) = monumentRepository.updateMonument(id = monumentId, favorite = favorite)
}