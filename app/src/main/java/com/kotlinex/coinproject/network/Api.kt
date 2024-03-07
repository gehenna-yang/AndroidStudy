package com.kotlinex.coinproject.network

import com.kotlinex.coinproject.network.model.CoinListResult
import retrofit2.http.GET

interface Api {

    @GET("public/ticker/ALL_KRW")
    suspend fun getCoinList() : CoinListResult

}