package com.example.quikcart.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.repos.CurrencyRepoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileViewModel(private val repo: CurrencyRepoInterface) :ViewModel() {
     var navigator:Navigator?=null
    fun navigateToOrdersFragment(){
        navigator?.navigateToOrdersFragment()
    }

    fun getCurrency(): Double {
        var usdRate: Double = 0.02102
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCurrency().collect { response ->
                usdRate = response.conversionRates["USD"] ?: 0.02102
            }
        }
        return usdRate
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