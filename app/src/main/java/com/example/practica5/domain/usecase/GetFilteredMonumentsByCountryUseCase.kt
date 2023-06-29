package com.example.practica5.domain.usecase

import com.example.practica5.domain.repository.MonumentRepository
import javax.inject.Inject
import javax.inject.Named

class GetFilteredMonumentsByCountryUseCase @Inject constructor(
    private val monumentRepository: MonumentRepository
) {
    private var country: String = ""
    suspend operator fun invoke() = monumentRepository.getFilteredMonuments(country)

    fun setCountry(country: String) {
        this.country = country
    }
}