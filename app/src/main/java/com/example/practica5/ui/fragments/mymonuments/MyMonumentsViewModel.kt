package com.example.practica5.ui.fragments.mymonuments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.datasource.Resource
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.domain.usecase.GetMyMonumentsUseCase
import com.example.practica5.utils.MonumentsConstant.ERROR_LOADING_MY_MONUMENTS
import com.example.practica5.utils.MonumentsConstant.ERROR_MY_MONUMENTS_FOUND
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyMonumentsViewModel @Inject constructor(private val getMyMonumentsUseCase: GetMyMonumentsUseCase): ViewModel() {
    private val myMonumentsMutableLiveData = MutableLiveData<List<MonumentVO>?>()
    fun getMyMonumentsList(): LiveData<List<MonumentVO>?> = myMonumentsMutableLiveData

    private val resourceLiveData = MutableLiveData<Resource<List<MonumentVO>, String>>()
    fun getResourceState(): LiveData<Resource<List<MonumentVO>, String>> = resourceLiveData

    init {
        getMyMonuments()
    }

    fun getMyMonuments() {
        viewModelScope.launch {
            resourceLiveData.value = Resource.Loading

            try {
                val result = getMyMonumentsUseCase()
                val monumentListVO = result?.map { MonumentMapper.mapMonumentBoToVo(it) }
                if (!result.isNullOrEmpty()) {
                    myMonumentsMutableLiveData.postValue(monumentListVO)
                    resourceLiveData.postValue(Resource.Success(monumentListVO as List<MonumentVO>))
                } else {
                    myMonumentsMutableLiveData.postValue(emptyList())
                    resourceLiveData.postValue(Resource.Error(ERROR_MY_MONUMENTS_FOUND))
                }
            } catch (e: Exception) {
                resourceLiveData.postValue(Resource.Error(ERROR_LOADING_MY_MONUMENTS))
            }
        }
    }
}