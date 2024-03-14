package com.kotlinex.coinproject.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kotlinex.coinproject.db.entity.SelectedCoinPriceEntity
import com.kotlinex.coinproject.network.model.RecentDataList
import com.kotlinex.coinproject.repository.DBRepository
import com.kotlinex.coinproject.repository.NetworkRepository
import java.util.Date
import java.util.Calendar

class GetCoinPriceWorkManager(val context: Context, worker: WorkerParameters)
    : CoroutineWorker(context, worker) {

    private val dbRepository = DBRepository()
    private val networkRepository = NetworkRepository()

    override suspend fun doWork(): Result {

        getAllInterestSelectedCoinData()

        return Result.success()
    }

    suspend fun getAllInterestSelectedCoinData() {
        val selectedCoinList = dbRepository.getSelectedCoinData()

        val timeStamp = Calendar.getInstance().time

        for (coin in selectedCoinList) {

            val result = networkRepository.getRecentCoinData(coin.coin_name)

            saveSelectedCoinPrice(
                coin.coin_name,
                result,
                timeStamp
            )

        }
    }

    fun saveSelectedCoinPrice(
        coinName: String,
        recentDataList: RecentDataList,
        timeStamp: Date
    ){

        val selectedCoinPriceEntity = SelectedCoinPriceEntity(
            0,
            coinName,
            recentDataList.data[0].transaction_date,
            recentDataList.data[0].type,
            recentDataList.data[0].units_traded,
            recentDataList.data[0].price,
            recentDataList.data[0].total,
            timeStamp
        )

        dbRepository.insertCoinPrice(selectedCoinPriceEntity)

    }

}