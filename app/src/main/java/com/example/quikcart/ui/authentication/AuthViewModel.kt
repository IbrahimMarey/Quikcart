package com.example.quikcart.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.entities.User
import com.example.quikcart.models.firebase.AuthRepository
import kotlinx.coroutines.launch


class AuthViewModel(private val authRepository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult: MutableLiveData<Boolean> = _signUpResult

    fun signUp(user: User) {
        viewModelScope.launch {
            _signUpResult.value = authRepository.signUpWithEmailAndPassword(user)
        }
    }
}