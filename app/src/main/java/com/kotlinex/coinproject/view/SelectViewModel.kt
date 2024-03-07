package com.kotlinex.coinproject.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kotlinex.coinproject.datamodel.Coin
import com.kotlinex.coinproject.datamodel.CoinList
import com.kotlinex.coinproject.repository.NetworkRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()

    private lateinit var coinListResult : ArrayList<CoinList>

    // LiveData
    private val _coinListResult = MutableLiveData<List<CoinList>>()
    val coinResult : LiveData<List<CoinList>> get() = _coinListResult

    fun getCoinList() = viewModelScope.launch {
        val result = networkRepository.getCoinList()

        coinListResult = ArrayList()

        for (coin in result.data) {

            try {
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data.get(coin.key))
                val gsonFromJson = gson.fromJson(gsonToJson, Coin::class.java)

                val coinresult = CoinList(coin.key, gsonFromJson)

                coinListResult.add(coinresult)

            } catch (e: java.lang.Exception) {
                Timber.e(e.toString())
            }
        }

        _coinListResult.value = coinListResult
    }

}