package com.example.doitapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity: AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Delayed startup of the main activity
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app's main activity
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))

            // Close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}