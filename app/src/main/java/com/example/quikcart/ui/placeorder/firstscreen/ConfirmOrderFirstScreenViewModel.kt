package com.example.quikcart.ui.placeorder.firstscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderFirstScreenViewModel@Inject constructor(private val repo: Repository):ViewModel() {
    lateinit var navigator: Navigator
    private val _uiState : MutableStateFlow<ViewState<List<AddressResponse>>> = MutableStateFlow(
        ViewState.Loading)
    val uiState : StateFlow<ViewState<List<AddressResponse>>> = _uiState

    fun getCustomerAddresses(customerID:Long)
    {
        viewModelScope.launch {
            _uiState.value=ViewState.Loading
            repo.getAllAddressesShopify(customerID).catch {
                _uiState.value = it.localizedMessage?.let { it1 -> ViewState.Error(it1) }!!
            }.collect{
                _uiState.value = ViewState.Success(it.addresses)
            }
        }
    }

    fun navigateToMapFragment(){
        navigator.navigateToMapFragment()
    }

}