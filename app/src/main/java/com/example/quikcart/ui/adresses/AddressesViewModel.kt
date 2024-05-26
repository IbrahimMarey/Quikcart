package com.example.quikcart.ui.adresses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.repos.appRepo.AppRepo
import com.example.quikcart.models.repos.appRepo.AppRepoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(val _appRepo:AppRepoInterface):ViewModel() {
    private val _addresses :MutableStateFlow<ViewState<List<AddressModel>>> = MutableStateFlow(ViewState.Loading)
    val addresses : StateFlow<ViewState<List<AddressModel>>> = _addresses

    init {
        getAllAddresses()
    }

    private fun getAllAddresses(){
        viewModelScope.launch(){
            _appRepo.getAllAddresses().catch {
                _addresses.value = ViewState.Error(it.message.toString())
            }.collect{
                _addresses.value = ViewState.Success(it)
            }
        }
    }

    fun insertAddress(addressModel: AddressModel){
        viewModelScope.launch(Dispatchers.IO) {
            _appRepo.insertAddress(addressModel)
            getAllAddresses()
        }
    }

    fun delAddress(addressModel: AddressModel){
        viewModelScope.launch(Dispatchers.IO) {
            _appRepo.delAddress(addressModel)
            getAllAddresses()
        }
    }
}