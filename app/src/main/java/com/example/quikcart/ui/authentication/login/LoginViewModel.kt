package com.example.quikcart.ui.authentication.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.Customers
import com.example.quikcart.models.entities.User
import com.example.quikcart.models.firebase.AuthenticationRepository
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel@Inject constructor(private val authRepository: AuthenticationRepository, private val repository: Repository) : ViewModel() {

    private val _loginState = MutableStateFlow<ViewState<String?>>(ViewState.Loading)
    val loginState = _loginState

    private val _customerCreationState = MutableStateFlow<ViewState<List<Customers>>>(ViewState.Loading)
    val customerCreationState = _customerCreationState
    fun login(user: User){
        viewModelScope.launch {
            _loginState.value = ViewState.Loading
            val success = authRepository.signInWithEmailAndPassword(user)
            if(success.isSuccess){
                val userId = success.getOrNull()
                _loginState.value = ViewState.Success(userId)

            }
        }
    }

    fun getCustomer() {
        viewModelScope.launch {
            _customerCreationState.value = ViewState.Loading
             repository.getCustomer().catch {
                     error->
                 _customerCreationState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
             }
                 .collect{
                     customer->
                     _customerCreationState.value = ViewState.Success(customer)
                 }

        }
    }
}