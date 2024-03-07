package com.kotlinex.coinproject.view.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.kotlinex.coinproject.MainActivity
import com.kotlinex.coinproject.R
import timber.log.Timber


// Splash view
// hadler -> 3sec late move MainActivity

class IntroActivity : AppCompatActivity() {

    private val viewModel : IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        Timber.d("onCreate")

        viewModel.checkFirstFlag()
        viewModel.first.observe(this, Observer {
            if(it){
                // 처음 접속이 아님
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // 처음 접속
            }
        })
    }
}