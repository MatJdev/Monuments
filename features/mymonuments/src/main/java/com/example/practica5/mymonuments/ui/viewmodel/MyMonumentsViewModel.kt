package com.example.practica5.mymonuments.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.common.Resource
import com.example.practica5.common.util.MonumentMapper
import com.example.practica5.model.vo.MonumentVO
import com.example.practica5.common.util.MonumentsConstant.ERROR_LOADING_MY_MONUMENTS
import com.example.practica5.common.util.MonumentsConstant.ERROR_MY_MONUMENTS_FOUND
import com.example.practica5.mymonuments.domain.GetMyMonumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyMonumentsViewModel @Inject constructor(private val getMyMonumentsUseCase: GetMyMonumentsUseCase): ViewModel() {

    private val resourceStateFlow = MutableStateFlow<Resource<List<MonumentVO>, String>>(Resource.Loading)
    fun getResourceState(): StateFlow<Resource<List<MonumentVO>, String>> = resourceStateFlow.asStateFlow()

    init {
        getMyMonuments()
    }

    fun getMyMonuments() {
        viewModelScope.launch {
            resourceStateFlow.value = Resource.Loading

            try {
                val result = getMyMonumentsUseCase()
                val monumentListVO = result?.map { MonumentMapper.mapMonumentBoToVo(it) }
                if (!result.isNullOrEmpty()) {
                    resourceStateFlow.value = Resource.Success(monumentListVO as List<MonumentVO>)
                } else {
                    resourceStateFlow.value = Resource.Error(ERROR_MY_MONUMENTS_FOUND)
                }
            } catch (e: Exception) {
                resourceStateFlow.value = Resource.Error(ERROR_LOADING_MY_MONUMENTS)
            }
        }
    }
}