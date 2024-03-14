package com.kotlinex.coinproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kotlinex.coinproject.background.GetCoinPriceWorkManager
import com.kotlinex.coinproject.view.main.MainActivity
import com.kotlinex.coinproject.databinding.ActivitySelectBinding
import com.kotlinex.coinproject.view.adapter.SelectAdapter
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectBinding

    private val viewModel : SelectViewModel by viewModels()

    private lateinit var selectAdapter: SelectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getCoinList()
        viewModel.coinResult.observe(this, Observer {
            selectAdapter = SelectAdapter(this, it)

            binding.coinListRV.adapter = selectAdapter
            binding.coinListRV.layoutManager = LinearLayoutManager(this)

            Timber.d(it.toString())
        })


        binding.laterTextArea.setOnClickListener {
            viewModel.setUpFirstFlag()
            viewModel.saveSelectedCoinList(selectAdapter.selectedCoinList)
        }

        viewModel.save.observe(this, Observer {
            if(it.equals("done")){
                // 최초 관심 코인을 설정한 시점
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                saveInterestCoinDataPeriod()
            }
        })
    }

    private fun saveInterestCoinDataPeriod() {
        val myWork = PeriodicWorkRequest.Builder(
            GetCoinPriceWorkManager::class.java, 15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "GetCoinPriceWorkManager",
            ExistingPeriodicWorkPolicy.KEEP,
            myWork
        )
    }

}