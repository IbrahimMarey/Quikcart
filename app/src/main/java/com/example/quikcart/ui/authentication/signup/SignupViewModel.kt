package com.example.quikcart.ui.authentication.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.User
import com.example.quikcart.models.firebase.AuthenticationRepository
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val authRepository: AuthenticationRepository, private val repository: Repository) : ViewModel() {

    private val _authState = MutableStateFlow<ViewState<String?>>(ViewState.Loading)
    val authState = _authState

    private val _customerCreationState = MutableStateFlow<ViewState<Boolean>>(ViewState.Loading)
    val customerCreationState = _customerCreationState
    fun signUp(user: User) {
        viewModelScope.launch {
            _authState.value = ViewState.Loading
            val result = authRepository.signUpWithEmailAndPassword(user)
            if (result.isSuccess) {
                val userId = result.getOrNull()
                _authState.value = ViewState.Success(userId)
                Log.i("TAG", "signUp: $userId")
            } else {
                _authState.value = ViewState.Error("Signup failed")
            }
        }
    }

    fun createCustomer(customerRequest: CustomerRequest) = viewModelScope.launch {
        _customerCreationState.value = ViewState.Loading
        try {
            val response = repository.postCustomer(customerRequest)

            if (response.isSuccessful) {
                _customerCreationState.value = ViewState.Success(true)
            } else {
                _customerCreationState.value = ViewState.Error("Failed to create customer")
            }
        } catch (e: Exception) {
            _customerCreationState.value = ViewState.Error("Network error occurred")
        }
    }
}