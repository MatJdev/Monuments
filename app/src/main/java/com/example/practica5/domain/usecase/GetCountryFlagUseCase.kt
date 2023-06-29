package com.example.practica5.domain.usecase

import com.example.practica5.domain.repository.MonumentRepository
import javax.inject.Inject

class GetCountryFlagUseCase @Inject constructor(private val monumentRepository: MonumentRepository) {
    private var countryCode: String = ""
    suspend operator fun invoke(): String {
        return monumentRepository.getCountryFlag(countryCode)
    }

    fun setCountryCode(countryCode: String) {
        this.countryCode = countryCode
    }
}