package com.kotlinex.coinproject.repository

import com.kotlinex.coinproject.App
import com.kotlinex.coinproject.db.CoinDatabase
import com.kotlinex.coinproject.db.entity.InterestCoinEntity

class DBRepository {

    val context = App.context()
    val db = CoinDatabase.getDatabase(context)

    // InterestCoin

    // get all coin data
    fun getAllCoinData() = db.interestCoinDAO().getAllData()

    // insert coin data
    fun insertCoinData(interestCoinEntity: InterestCoinEntity) = db.interestCoinDAO().insert(interestCoinEntity)

    // update coin data
    fun updateCoinData(interestCoinEntity: InterestCoinEntity) = db.interestCoinDAO().update(interestCoinEntity)

    // my selected coin data
    fun getSelectedCoinData() = db.interestCoinDAO().getSelectedData()
}