package com.example.practica5.commonfeatures.domain

import com.example.practica5.data.repository.monuments.MonumentRepository
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