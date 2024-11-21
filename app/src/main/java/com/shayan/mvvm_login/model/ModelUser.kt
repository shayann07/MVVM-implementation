package com.shayan.mvvm_login.model

data class ModelUser(
    val firstName: String,    
    val lastName: String,
    val phone: String,
    val password: String,
    val cPass: String,
    val token: String? = null // Token (only used after successful login)
)