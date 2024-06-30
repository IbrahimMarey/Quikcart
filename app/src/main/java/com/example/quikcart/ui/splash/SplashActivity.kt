package com.example.quikcart.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.quikcart.R
import com.example.quikcart.onboarding.OnBoardingActivity
import com.example.quikcart.ui.MainActivity
import com.example.quikcart.utils.PreferencesUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val preferencesUtils = PreferencesUtils.getInstance(this)
             val userId = preferencesUtils.getCustomerId()
            val intent: Intent = if (userId == 0L) {
                Intent(this, OnBoardingActivity::class.java)

            } else {
                Intent(this, MainActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }, 3000)


    }
}
