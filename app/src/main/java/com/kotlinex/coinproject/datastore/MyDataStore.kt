package com.kotlinex.coinproject.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.kotlinex.coinproject.App

class MyDataStore {

    private val context = App.context()

    companion object {

        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("user_pref")

    }

    private val mDataStore : DataStore<Preferences> = context.dataStore

    private val FIRST_FLAG = booleanPreferencesKey("FIRST_FLAG")

    // 최초 접속이 아님으로 설정
    suspend fun  setupFirstData() {
        mDataStore.edit { preferences ->
            preferences[FIRST_FLAG] = true
        }
    }

    suspend fun getFirstData() : Boolean {
        var value = false

        mDataStore.edit { preferences ->
            value = preferences[FIRST_FLAG] ?: false
        }

        return  value
    }

}