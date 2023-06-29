package com.example.practica5.domain.usecase

import com.example.practica5.domain.repository.MonumentRepository
import javax.inject.Inject

class GetUniqueCountriesUseCase @Inject constructor(private val monumentRepository: MonumentRepository)  {
    suspend operator fun invoke(): List<String> {
        return monumentRepository.getUniqueCountries()
    }
}