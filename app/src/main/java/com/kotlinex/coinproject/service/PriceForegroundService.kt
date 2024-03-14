package com.kotlinex.coinproject.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.kotlinex.coinproject.R
import com.kotlinex.coinproject.datamodel.Coin
import com.kotlinex.coinproject.datamodel.CoinList
import com.kotlinex.coinproject.repository.NetworkRepository
import com.kotlinex.coinproject.view.main.MainActivity
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.Random

class PriceForegroundService : Service() {

    private val networkRepository = NetworkRepository()

    private val NOTIFICATION_ID = 10000

    lateinit var job : Job

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){
            "START" -> {
                // 노티 시작
                job = CoroutineScope(Dispatchers.Default).launch {
                    while (true) {
                        startForeground(NOTIFICATION_ID, makeNotification())
                        delay(1000)
                    }
                }
            }

            "STOP" -> {
                // 노티 해제
                try {
                    job.cancel()
                    stopForeground(true)
                    stopSelf()
                } catch (e : java.lang.Exception) {
                }
            }
       }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    suspend fun makeNotification() : Notification {

        val result = getAllCoinList()

        val randomNum = Random().nextInt(result.size)

        val title = result[randomNum].coinName
        val content = result[randomNum].coinInfo.fluctate_24H

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent : PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
            .setContentTitle("코인 이름 : $title")
            .setContentText("코인 가격 : $content")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        return builder.build()

    }

    suspend fun getAllCoinList() : ArrayList<CoinList> {
        val result = networkRepository.getCoinList()

        val coinListResult = ArrayList<CoinList>()

        for (coin in result.data) {

            try {
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data.get(coin.key))
                val gsonFromJson = gson.fromJson(gsonToJson, Coin::class.java)

                val coinresult = CoinList(coin.key, gsonFromJson)

                coinListResult.add(coinresult)

            } catch (e: java.lang.Exception) {
                Timber.e(e.toString())
            }
        }

        return coinListResult
    }

}