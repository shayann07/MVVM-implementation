package com.shayan.mvvm_login.model

data class ModelUser(
    val firstName: String,    // User's first name
    val lastName: String,     // User's last name
    val phone: String,        // User's phone number
    val password: String,     // User's password
    val cPass: String,        // Confirm password
    val token: String? = null // Token (only used after successful login)
)