package com.kotlinex.coinproject.repository

import com.kotlinex.coinproject.network.Api
import com.kotlinex.coinproject.network.ApiInstance

class NetworkRepository {

    private val client = ApiInstance.getInstance().create(Api::class.java)

    suspend fun getCoinList() = client.getCoinList()
}