package com.shayan.mvvm_login.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shayan.mvvm_login.model.ModelUser
import com.shayan.mvvm_login.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val registerRepository = RegisterRepository(application)

    fun registerUser(
        user: ModelUser, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            registerRepository.registerUser(user, onSuccess, onError)
        }
    }
}
