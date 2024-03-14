package com.kotlinex.coinproject.datamodel

data class RecentData(
    val transaction_date: String,
    val type: String,
    val units_traded: String,
    val price: String,
    val total: String
)
