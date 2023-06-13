package com.example.practica5.ui.fragments.createMonument

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica5.data.mapper.MonumentMapper
import com.example.practica5.data.repository.MonumentRepositoryFactory
import com.example.practica5.domain.model.vo.ImageVO
import com.example.practica5.domain.model.vo.LocationVO
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.domain.usecase.GetCountryFlagUseCase
import com.example.practica5.domain.usecase.InsertMonumentUseCase
import com.example.practica5.utils.MonumentsUtils.getCountryCodeFromLocation
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class CreateMonumentViewModel : ViewModel() {
    private val locationMutableLiveData = MutableLiveData<LatLng>()
    val locationLiveData: LiveData<LatLng> = locationMutableLiveData

    private val imageListMutableLiveData = MutableLiveData<MutableList<ImageVO>>()
    val imageListLiveData: LiveData<MutableList<ImageVO>> get() = imageListMutableLiveData

    private val insertionCompleteLiveData = MutableLiveData<Boolean>()
    fun getInsertionComplete(): LiveData<Boolean> = insertionCompleteLiveData

    fun loadLocation(location: LatLng) {
        locationMutableLiveData.value = location
    }

    fun createNewMonument(name: String, desc: String, city: String, country: String, context: Context) {
        viewModelScope.launch {
            val lat = locationMutableLiveData.value?.latitude ?: 0.0
            val longitude = locationMutableLiveData.value?.longitude ?: 0.0
            val countryCode = getCountryCodeFromLocation(context, lat, longitude) ?: ""
            val countryFlag = getMonumentCountryFlag(countryCode)
            val newMonument = MonumentVO(
                name = name,
                city = city,
                description = desc,
                location = LocationVO(
                    locationMutableLiveData.value?.latitude ?: 0.0,
                    locationMutableLiveData.value?.longitude ?: 0.0
                ),
                images = imageListMutableLiveData.value ?: emptyList(),
                isFromMyMonuments = true,
                country = country,
                countryCode = countryCode,
                countryFlag = countryFlag
            )

            val insertMonumentUseCase = InsertMonumentUseCase(MonumentRepositoryFactory.monumentRepository, MonumentMapper.mapMonumentVoToBo(newMonument))
            insertMonumentUseCase()
            imageListMutableLiveData.value = mutableListOf()
            insertionCompleteLiveData.value = true
        }
    }

    private suspend fun getMonumentCountryFlag(countryCode: String): String {
        val getCountryFlagUseCase = GetCountryFlagUseCase(MonumentRepositoryFactory.monumentRepository, countryCode)
        return getCountryFlagUseCase()
    }

    fun addImage(imageUri: String) {
        val currentList = imageListMutableLiveData.value ?: mutableListOf()
        currentList.add(ImageVO(url = imageUri))
        imageListMutableLiveData.value = currentList
    }

    fun setLocation(location: LatLng) {
        locationMutableLiveData.value = location
    }

    fun deleteImage(image: ImageVO): Int {
        val currentList = imageListMutableLiveData.value ?: mutableListOf()
        val imgPosition = currentList.indexOf(image)
        currentList.remove(image)
        imageListMutableLiveData.value = currentList
        return imgPosition
    }

    fun setInsertionToFalse() {
        insertionCompleteLiveData.value = false
    }
}