package com.example.practica5.commonfeatures.domain

import com.example.practica5.data.repository.monuments.MonumentRepository
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_ID
import com.example.practica5.common.util.MonumentsConstant.MONUMENT_NAME
import com.example.practica5.model.bo.monument.MonumentBO
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