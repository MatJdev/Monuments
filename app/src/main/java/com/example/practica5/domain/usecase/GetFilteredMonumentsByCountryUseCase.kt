package com.example.practica5.domain.usecase

import com.example.practica5.domain.repository.MonumentRepository

class GetFilteredMonumentsByCountryUseCase(
    private val monumentRepository: MonumentRepository,
    private val country: String
) {
    suspend operator fun invoke() = monumentRepository.getFilteredMonuments(country)
}