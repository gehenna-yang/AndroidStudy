package com.kotlinex.coinproject.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlinex.coinproject.datamodel.UpDownDataSet
import com.kotlinex.coinproject.db.entity.InterestCoinEntity
import com.kotlinex.coinproject.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val dbRepository = DBRepository()

    lateinit var selectedCoinList : LiveData<List<InterestCoinEntity>>

    private val _arr15min = MutableLiveData<List<UpDownDataSet>>()
    val arr15min : LiveData<List<UpDownDataSet>>
        get() = _arr15min

    private val _arr30min = MutableLiveData<List<UpDownDataSet>>()
    val arr30min : LiveData<List<UpDownDataSet>>
        get() = _arr30min

    private val _arr45min = MutableLiveData<List<UpDownDataSet>>()
    val arr45min : LiveData<List<UpDownDataSet>>
        get() = _arr45min

    fun getAllInterestCoinData() = viewModelScope.launch {
        val coinList = dbRepository.getAllCoinData().asLiveData()
        selectedCoinList = coinList
    }

    fun updateInterestCoinData(interestCoinEntity: InterestCoinEntity) = viewModelScope.launch(Dispatchers.IO) {
        if(interestCoinEntity.selected) {
            interestCoinEntity.selected = false
        } else {
            interestCoinEntity.selected = true
        }
        dbRepository.updateCoinData(interestCoinEntity)
    }

    fun getAllSelectedCoinData() = viewModelScope.launch(Dispatchers.IO) {

        val selectedCoinList = dbRepository.getSelectedCoinData()

        val arr15m = ArrayList<UpDownDataSet>()
        val arr30m = ArrayList<UpDownDataSet>()
        val arr45m = ArrayList<UpDownDataSet>()

        for(data in selectedCoinList) {
            val coinName = data.coin_name
            val oneCoinData = dbRepository.getSelectedCoinPrice(coinName).reversed()

            val size = oneCoinData.size

            if(size > 1) {
                val changePrice = oneCoinData[0].price.toDouble() - oneCoinData[1].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changePrice.toString()
                )
                arr15m.add(upDownDataSet)
            }
            if(size > 2) {
                val changePrice = oneCoinData[0].price.toDouble() - oneCoinData[2].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changePrice.toString()
                )
                arr30m.add(upDownDataSet)
            }
            if(size > 3) {
                val changePrice = oneCoinData[0].price.toDouble() - oneCoinData[3].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changePrice.toString()
                )
                arr45m.add(upDownDataSet)
            }
        }

        withContext(Dispatchers.Main) {
            _arr15min.value = arr15m
            _arr30min.value = arr30m
            _arr45min.value = arr45m
        }

    }

}