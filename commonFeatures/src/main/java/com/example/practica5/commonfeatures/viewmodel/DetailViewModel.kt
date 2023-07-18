package com.example.practica5.commonfeatures.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.common.util.MonumentMapper
import com.example.practica5.commonfeatures.domain.DeleteMonumentUseCase
import com.example.practica5.model.vo.MonumentVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private var deleteMonumentUseCase: DeleteMonumentUseCase): ViewModel() {
    private val monumentMutableStateFlow = MutableStateFlow<MonumentVO?>(null)
    fun getMonument(): StateFlow<MonumentVO?> = monumentMutableStateFlow.asStateFlow()

    private val removeCompleteStateFlow = MutableStateFlow(false)
    fun getRemoveComplete(): StateFlow<Boolean> = removeCompleteStateFlow.asStateFlow()

    fun loadData(monument: MonumentVO) {
        monumentMutableStateFlow.value = monument
    }

    fun deleteMonument(monument: MonumentVO) {
        viewModelScope.launch {
            deleteMonumentUseCase.setMonument(MonumentMapper.mapMonumentVoToBo(monument))
            deleteMonumentUseCase.invoke()
            removeCompleteStateFlow.value = true
        }
    }

    fun setDeleteToFalse() {
        removeCompleteStateFlow.value = false
    }
}