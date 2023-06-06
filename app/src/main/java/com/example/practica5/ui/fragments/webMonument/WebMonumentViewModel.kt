package com.example.practica5.ui.fragments.webMonument

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practica5.R
import com.example.practica5.domain.model.vo.MonumentVO
import com.example.practica5.utils.MonumentsConstant.EMPTY_INFO

class WebMonumentViewModel : ViewModel() {
    private val webMonumentLiveData = MutableLiveData<MonumentVO?>(null)
    fun getWebMonument(): LiveData<MonumentVO?> = webMonumentLiveData

    fun loadMonument(monument: MonumentVO) {
        webMonumentLiveData.postValue(monument)
    }

    fun getWebUrl(context: Context): String {
        val monument = getWebMonument().value
        return if (monument?.urlExtraInformation.isNullOrEmpty()) {
            context.getString(R.string.webMonument__empty_url_info, monument?.name)
        } else {
            monument?.urlExtraInformation ?: EMPTY_INFO
        }
    }

    fun getEmailMessage(context: Context): String {
        val monument = getWebMonument().value
        return context.getString(
            R.string.webMonument__message_email,
            monument?.name,
            monument?.city,
            monument?.urlExtraInformation
        )
    }

    fun clearLiveData() {
        webMonumentLiveData.value = null
    }
}