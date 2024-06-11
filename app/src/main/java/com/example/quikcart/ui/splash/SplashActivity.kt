package com.example.quikcart.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quikcart.R
import com.example.quikcart.ui.MainActivity
import com.example.quikcart.ui.authentication.AuthenticationActivity
import com.example.quikcart.utils.PreferencesUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val preferencesUtils = PreferencesUtils.getInstance(this)
        val userId = preferencesUtils.getCustomerId()
        val intent: Intent = if (userId == 0L) {
            Intent(this, AuthenticationActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
