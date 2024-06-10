package com.example.quikcart.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.CategoryItem
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<SmartCollectionsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<SmartCollectionsItem>>> = _uiState
    val couponsList = ArrayList<SlideModel>()

    init {
        getBrands()
        getCoupon()
    }

    fun getCategories():List<CategoryItem>{
        return repo.getCategories()
    }

    private fun getBrands() {
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            repo.getBrands().catch { error ->
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { smartCollectionsItem ->
                _uiState.value = ViewState.Success(smartCollectionsItem)
                Log.e("TAG", "getBrands: ${smartCollectionsItem[0].title}", )
           }
        }
    }

    private fun getCoupon()
    {
        couponsList.add(SlideModel("https://t3.ftcdn.net/jpg/03/36/91/14/360_F_336911489_vQzdGPLdY0aNYXdu5rK7UIwwiEksYJgK.jpg","Click To Apply"))
        couponsList.add(SlideModel("https://static.vecteezy.com/system/resources/previews/004/141/832/non_2x/golden-30-percent-off-flat-cartoon-style-logo-concept-30-percent-sale-isolated-icon-on-black-background-thirty-percent-discount-for-business-illustration-vector.jpg","Click To Apply"))
        couponsList.add(SlideModel("https://static.vecteezy.com/system/resources/thumbnails/004/141/666/small_2x/golden-50-percent-off-flat-cartoon-style-logo-concept-50-percent-sale-isolated-icon-on-black-background-fifty-percent-discount-for-business-illustration-vector.jpg","Click To Apply"))
    }
}