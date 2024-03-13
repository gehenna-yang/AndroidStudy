package com.kotlinex.coinproject.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kotlinex.coinproject.R
import com.kotlinex.coinproject.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationView.setupWithNavController(navController)
    }
}