package com.example.practica5.commonfeatures.domain

import com.example.practica5.data.repository.monuments.MonumentRepository
import javax.inject.Inject

class GetFilteredMonumentsByCountryUseCase @Inject constructor(
    private val monumentRepository: MonumentRepository
) {
    private var country: String = ""
    suspend operator fun invoke() = monumentRepository.getFilteredMonuments(country)

    fun setCountry(country: String) {
        this.country = country
    }
}