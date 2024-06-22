package com.example.quikcart.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.quikcart.R
import com.example.quikcart.ui.authentication.AuthenticationActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)


        val layouts = intArrayOf(
            R.layout.onboarding_screen1,
            R.layout.onboarding_screen2,
            R.layout.onboarding_screen3
        )

        viewPager = findViewById(R.id.view_pager)
        adapter = OnboardingAdapter(this, layouts)
        viewPager.adapter = adapter

        val btnNext = findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            val current = viewPager.currentItem + 1
            if (current < layouts.size) {
                viewPager.currentItem = current
            } else {
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
            }
        }

    }
}