package com.example.practica5.domain.usecase

import com.example.practica5.domain.repository.MonumentRepository

class GetUniqueCountriesUseCase(private val monumentRepository: MonumentRepository)  {
    suspend operator fun invoke(): List<String> {
        return monumentRepository.getUniqueCountries()
    }
}