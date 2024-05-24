package com.example.quikcart.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.repos.BrandRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val brandRepo: BrandRepo) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<SmartCollectionsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<SmartCollectionsItem>>> = _uiState

    init {
        getBrands()
    }

    private fun getBrands() {
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            brandRepo.getBrands().catch { error ->
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { smartCollectionsItem ->
                _uiState.value = ViewState.Success(smartCollectionsItem)
                Log.e("TAG", "getBrands: ${smartCollectionsItem[0].title}", )
           }
        }
    }
}