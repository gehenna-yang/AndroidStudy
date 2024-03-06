package com.kotlinex.coinproject.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinex.coinproject.repository.NetworkRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()

    fun getCoinList() = viewModelScope.launch {
        val result = networkRepository.getCoinList()
        Timber.d(result.toString())
    }

}