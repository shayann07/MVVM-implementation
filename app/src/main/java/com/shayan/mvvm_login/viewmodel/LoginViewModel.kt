package com.shayan.mvvm_login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.mvvm_login.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var loginRepository = LoginRepository(application)

    fun loginUser(
        phone: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            loginRepository.loginUser(phone, password, onSuccess, onError)
        }
    }
}
