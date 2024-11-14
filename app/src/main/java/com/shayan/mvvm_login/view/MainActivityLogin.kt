package com.shayan.mvvm_login.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shayan.mvvm_login.databinding.ActivityMainLoginBinding
import com.shayan.mvvm_login.viewmodel.LoginViewModel

class MainActivityLogin : AppCompatActivity() {

    private lateinit var binding: ActivityMainLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("PrefsDatabase", MODE_PRIVATE)

        binding.apply {

            signupScreen.setOnClickListener {
                startActivity(Intent(this@MainActivityLogin, MainActivityRegister::class.java))
            }

            loginButton.setOnClickListener {
                val phone = phoneEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (phone.isNotEmpty() && password.isNotEmpty()) {
                    loginViewModel.loginUser(phone, password, { token ->
                        saveToken(token)
                        startActivity(Intent(this@MainActivityLogin, MainActivityHome::class.java))
                        finish()
                    }, { errorMessage ->
                        Toast.makeText(this@MainActivityLogin, errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    })
                } else {
                    Toast.makeText(
                        this@MainActivityLogin,
                        "Please enter phone and password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("user_token", token).apply()
    }
}
