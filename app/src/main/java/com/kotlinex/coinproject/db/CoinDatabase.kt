package com.kotlinex.coinproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kotlinex.coinproject.db.dao.InterestCoinDAO
import com.kotlinex.coinproject.db.dao.SelectedCoinPriceDAO
import com.kotlinex.coinproject.db.entity.DateConverter
import com.kotlinex.coinproject.db.entity.InterestCoinEntity
import com.kotlinex.coinproject.db.entity.SelectedCoinPriceEntity


@Database(entities = [InterestCoinEntity::class, SelectedCoinPriceEntity::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class CoinDatabase: RoomDatabase() {

    abstract fun interestCoinDAO() : InterestCoinDAO
    abstract fun selectedCoinDAO() : SelectedCoinPriceDAO

    companion object {
        @Volatile
        private var INSTANCE : CoinDatabase? = null

        fun getDatabase(
            context: Context
        ) : CoinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoinDatabase::class.java,
                    "coin_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}