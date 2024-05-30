package com.example.quikcart.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: Repository) :ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<ProductsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<ProductsItem>>> = _uiState

      fun getProducts() {
         viewModelScope.launch {
             _uiState.value = ViewState.Loading
             repo.getProducts().catch {
                  error->
                 _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
             }
                 .collect {productsItem->
                     _uiState.value = ViewState.Success(productsItem)
                     Log.e("TAG", "getBrands: ${productsItem[0].title}", )
                 }
         }
     }
}