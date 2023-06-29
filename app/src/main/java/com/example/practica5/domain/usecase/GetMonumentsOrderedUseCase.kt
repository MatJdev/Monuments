package com.example.practica5.domain.usecase

import com.example.practica5.domain.model.bo.MonumentBO
import com.example.practica5.domain.repository.MonumentRepository
import com.example.practica5.utils.MonumentsConstant.MONUMENT_ID
import com.example.practica5.utils.MonumentsConstant.MONUMENT_NAME
import javax.inject.Inject

class GetMonumentsOrderedUseCase @Inject constructor(private val monumentRepository: MonumentRepository) {
    suspend operator fun invoke(orderBy: OrderBy): List<MonumentBO> {
        return when (orderBy) {
            OrderBy.LATITUDE -> monumentRepository.getMonumentsOrderedByNtoS()
            OrderBy.LONGITUDE -> monumentRepository.getMonumentsOrderedByEtoW()
            OrderBy.ID -> monumentRepository.getSortedMonuments(MONUMENT_ID)
            OrderBy.NAME -> monumentRepository.getSortedMonuments(MONUMENT_NAME)
        }
    }
}

enum class OrderBy {
    LATITUDE,
    LONGITUDE,
    ID,
    NAME
}