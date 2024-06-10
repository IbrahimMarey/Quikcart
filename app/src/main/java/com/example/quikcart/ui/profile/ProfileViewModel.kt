package com.example.quikcart.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.repos.CurrencyRepoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(private val repo: CurrencyRepoInterface) :ViewModel() {
    private var _usdCurrency = MutableStateFlow<ViewState<Double>>(ViewState.Loading)
    var usdCurrency: StateFlow<ViewState<Double>> = _usdCurrency
    var navigator:Navigator?=null
    fun navigateToOrdersFragment(){
        navigator?.navigateToOrdersFragment()
    }

    init {
        getCurrency()
    }
    private fun getCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCurrency().collect { response ->
                _usdCurrency.value = ViewState.Success(response.conversionRates["USD"]?: 0.02102)
            }
        }
    }
}

class ProfileModelFactory(private val currencyRepo: CurrencyRepoInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
        {
            ProfileViewModel(currencyRepo) as T
        }else{
            throw IllegalArgumentException("Not Found")
        }
    }
}