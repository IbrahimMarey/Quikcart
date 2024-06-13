package com.example.quikcart.ui.authentication.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.Customers
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.User
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.models.firebase.AuthenticationRepository
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private val repository: Repository
) : ViewModel() {

    private val _loginState = MutableStateFlow<ViewState<String?>>(ViewState.Loading)
    val loginState = _loginState

    private val _customerCreationState = MutableStateFlow<ViewState<List<Customers>>>(ViewState.Loading)
    val customerCreationState = _customerCreationState

    private val _allDraftOrder = MutableStateFlow<ViewState<List<DraftOrder>>>(ViewState.Loading)
    val allDraftOrder = _allDraftOrder

    private val _uiState = MutableStateFlow<ViewState<List<ProductsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<ProductsItem>>> = _uiState

    fun login(user: User) {
        viewModelScope.launch {
            _loginState.value = ViewState.Loading
            val success = authRepository.signInWithEmailAndPassword(user)
            if (success.isSuccess) {
                val userId = success.getOrNull()
                _loginState.value = ViewState.Success(userId)
            }
        }
    }

    fun getCustomer() {
        viewModelScope.launch {
            _customerCreationState.value = ViewState.Loading
            repository.getCustomer().catch { error ->
                _customerCreationState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { customer ->
                _customerCreationState.value = ViewState.Success(customer)
            }
        }
    }
    fun getProducts(){
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            repository.getProducts().catch { error ->
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { productsItem ->
                _uiState.value = ViewState.Success(productsItem)
                Log.e("TAG", "getProduct: ${productsItem[0].title}")
            }
        }
    }

    fun getAllDraftOrder() {
        viewModelScope.launch {
            _allDraftOrder.value = ViewState.Loading
            repository.getAllDraftOrders().catch { error ->
                _allDraftOrder.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { draftOrder ->
                _allDraftOrder.value = ViewState.Success(draftOrder.draftOrders)
            }
        }
    }
    fun inertProducts(productsItem: ProductsItem) {
        viewModelScope.launch {
            repository.inertProduct(productsItem)
        }

    }
}
