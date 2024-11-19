package com.shayan.mvvm_login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shayan.mvvm_login.databinding.ActivityMainRegisterBinding
import com.shayan.mvvm_login.model.ModelUser
import com.shayan.mvvm_login.viewmodel.AuthViewModel

class MainActivityRegister : AppCompatActivity() {

    private lateinit var binding: ActivityMainRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            signupButton.setOnClickListener {
                val firstNameInput = firstName.text.toString()
                val lastNameInput = lastName.text.toString()
                val phoneInput = phoneEditText.text.toString()
                val passwordInput = passwordEditText.text.toString()
                val confirmPasswordInput = confirmPasswordEditText.text.toString()

                if (firstNameInput.isNotEmpty() && lastNameInput.isNotEmpty() && phoneInput.isNotEmpty() && passwordInput == confirmPasswordInput) {

                    val modelUser = ModelUser(
                        firstNameInput,
                        lastNameInput,
                        phoneInput,
                        passwordInput,
                        confirmPasswordInput
                    )
                    authViewModel.registerUser(modelUser, {
                        Toast.makeText(
                            this@MainActivityRegister, "Registration successful", Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@MainActivityRegister, MainActivityLogin::class.java
                            )
                        )
                        finish()
                    }, { errorMessage ->
                        Toast.makeText(this@MainActivityRegister, errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    })
                } else {
                    Toast.makeText(
                        this@MainActivityRegister,
                        "Please fill all fields correctly",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
