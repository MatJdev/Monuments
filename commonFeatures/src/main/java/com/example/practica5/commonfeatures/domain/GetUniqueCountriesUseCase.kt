package com.example.practica5.commonfeatures.domain

import com.example.practica5.data.repository.monuments.MonumentRepository
import javax.inject.Inject

class GetUniqueCountriesUseCase @Inject constructor(private val monumentRepository: MonumentRepository)  {
    suspend operator fun invoke(): List<String> {
        return monumentRepository.getUniqueCountries()
    }
}