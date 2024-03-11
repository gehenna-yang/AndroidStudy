package com.kotlinex.coinproject.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kotlinex.coinproject.datamodel.Coin
import com.kotlinex.coinproject.datamodel.CoinList
import com.kotlinex.coinproject.datastore.MyDataStore
import com.kotlinex.coinproject.db.entity.InterestCoinEntity
import com.kotlinex.coinproject.repository.DBRepository
import com.kotlinex.coinproject.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SelectViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()
    private val dbRepository = DBRepository()

    private lateinit var coinListResult : ArrayList<CoinList>

    // LiveData
    private val _coinListResult = MutableLiveData<List<CoinList>>()
    val coinResult : LiveData<List<CoinList>> get() = _coinListResult

    private val _saved = MutableLiveData<String>()
    val save : LiveData<String> get() = _saved

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

    fun setUpFirstFlag() = viewModelScope.launch {
        MyDataStore().setupFirstData()
    }

    fun saveSelectedCoinList(selectCoinList: ArrayList<String>) = viewModelScope.launch (Dispatchers.IO) {
        // 전체 코인 데이터 중 선택된 코인을 확인하여 저장
        for (coin in coinListResult) {
            val selected = selectCoinList.contains(coin.coinName)
            val interestCoin = InterestCoinEntity(
                0,
                coin.coinName,
                coin.coinInfo.opening_price,
                coin.coinInfo.closing_price,
                coin.coinInfo.min_price,
                coin.coinInfo.max_price,
                coin.coinInfo.units_traded,
                coin.coinInfo.acc_trade_value,
                coin.coinInfo.fluctate_24H,
                coin.coinInfo.acc_trade_value_24H,
                coin.coinInfo.units_traded_24H,
                coin.coinInfo.fluctate_rate_24H,
                coin.coinInfo.prev_closing_price,
                selected
            )

            interestCoin.let {
                dbRepository.insertCoinData(it)
            }
        }

        withContext(Dispatchers.Main) {
            _saved.value = "done"
        }
    }

}