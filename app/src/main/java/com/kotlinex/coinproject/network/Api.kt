package com.kotlinex.coinproject.network

import com.kotlinex.coinproject.network.model.CoinListResult
import com.kotlinex.coinproject.network.model.RecentDataList
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("public/ticker/ALL_KRW")
    suspend fun getCoinList() : CoinListResult

    @GET("public/transaction_history/{coin}_KRW")
    suspend fun getRecentCoin(@Path("coin") coin: String) : RecentDataList

}