package com.shayan.mvvm_login.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.shayan.mvvm_login.databinding.ActivityMainSplashBinding

class MainActivitySplash : AppCompatActivity() {

    private lateinit var binding: ActivityMainSplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("PrefsDatabase", MODE_PRIVATE)

        val token = sharedPreferences.getString("user_token", null)
        Handler().postDelayed({
            if (token == null) {
                startActivity(Intent(this@MainActivitySplash, MainActivityLogin::class.java))
            } else {
                startActivity(Intent(this@MainActivitySplash, MainActivityHome::class.java))
            }
            finish()
        }, 5650)
    }
}