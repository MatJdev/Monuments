package com.example.practica5.domain.usecase

import com.example.practica5.domain.repository.MonumentRepository

class GetCountryFlagUseCase(private val monumentRepository: MonumentRepository, private val countryCode: String) {
    suspend operator fun invoke(): String {
        return monumentRepository.getCountryFlag(countryCode)
    }
}