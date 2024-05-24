package com.example.quikcart.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.User
import com.example.quikcart.models.firebase.AuthRepositoryImp
import com.example.quikcart.models.firebase.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthenticationRepository) : ViewModel() {

    private val _signUpState = MutableStateFlow<ViewState<Boolean>>(ViewState.Loading)
    val signUpState = _signUpState

    private val _signInState = MutableStateFlow<ViewState<Boolean>>(ViewState.Loading)
    val signInState = _signInState

    fun signUp(user: User) {
        viewModelScope.launch {
            _signUpState.value = ViewState.Loading
            val success = authRepository.signUpWithEmailAndPassword(user)
            _signUpState.value = ViewState.Success(success)
        }
    }
    fun login(user: User){
        viewModelScope.launch {
            _signInState.value = ViewState.Loading
            val success = authRepository.signInWithEmailAndPassword(user)
            _signInState.value = ViewState.Success(success)
        }
    }
}