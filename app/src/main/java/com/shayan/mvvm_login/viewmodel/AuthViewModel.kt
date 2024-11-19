package com.shayan.mvvm_login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.mvvm_login.model.ModelUser
import com.shayan.mvvm_login.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    fun loginUser(
        phone: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            authRepository.loginUser(phone, password, onSuccess, onError)
        }
    }

    fun registerUser(
        user: ModelUser, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            authRepository.registerUser(user, onSuccess, onError)
        }
    }
}
