package com.kotlinex.coinproject.network.model

import com.kotlinex.coinproject.datamodel.RecentData

data class RecentDataList(
    val status : String,
    val data : List<RecentData>
)
