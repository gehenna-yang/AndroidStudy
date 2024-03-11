package com.kotlinex.coinproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlinex.coinproject.db.dao.InterestCoinDAO
import com.kotlinex.coinproject.db.entity.InterestCoinEntity


@Database(entities = [InterestCoinEntity::class], version = 1)
abstract class CoinDatabase: RoomDatabase() {

    abstract  fun interestCoinDAO() : InterestCoinDAO

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