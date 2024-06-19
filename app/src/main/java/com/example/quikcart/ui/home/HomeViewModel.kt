package com.example.quikcart.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.CategoryItem
import com.example.quikcart.models.entities.CouponModel
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<SmartCollectionsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<SmartCollectionsItem>>> = _uiState
    /*val couponsList = ArrayList<SlideModel>()
    val couponsIDs = ArrayList<String>()*/
    private val _couponsList = MutableLiveData<List<SlideModel>>()
    val couponsList: LiveData<List<SlideModel>> get() = _couponsList
    val couponsIDs = ArrayList<String>()

    init {
        getBrands()
        getCoupon()
    }

    fun getCategories():List<CategoryItem>{
        return repo.getCategories()
    }

    private fun getBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = ViewState.Loading
            repo.getBrands().catch { error ->
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { smartCollectionsItem ->
                _uiState.value = ViewState.Success(smartCollectionsItem)
           }
        }
    }
    fun getCoupon() {
        delCoupons()
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCoupons().collect { coupons ->
                val newCouponsList = mutableListOf<SlideModel>()
                couponsIDs.clear()
                getCouponImages(coupons.priceRules, newCouponsList)
                saveCouponsLocally(coupons.priceRules)
                _couponsList.postValue(newCouponsList)
            }
        }
    }

    private fun delCoupons() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllCoupons()
        }
    }

    private fun saveCouponsLocally(coupons: List<PriceRule>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAllCoupons(coupons)
        }
    }

    private fun getCouponImages(coupons: List<PriceRule>, newCouponsList: MutableList<SlideModel>) {
        for (item in coupons) {
            couponsIDs.add(item.id.toString())
            if (item.valueType == "percentage") {
                newCouponsList.add(addImgPercentage(item))
            } else {
                newCouponsList.add(addImgFixedAmount(item))
            }
        }
    }

    private fun addImgPercentage(coupon: PriceRule): SlideModel {
        val img = when (coupon.value) {
            "-20.0" -> "https://t3.ftcdn.net/jpg/03/36/91/14/360_F_336911489_vQzdGPLdY0aNYXdu5rK7UIwwiEksYJgK.jpg"
            "-30.0" -> "https://static.vecteezy.com/system/resources/previews/004/141/832/non_2x/golden-30-percent-off-flat-cartoon-style-logo-concept-30-percent-sale-isolated-icon-on-black-background-thirty-percent-discount-for-business-illustration-vector.jpg"
            "-50.0" -> "https://static.vecteezy.com/system/resources/thumbnails/004/141/666/small_2x/golden-50-percent-off-flat-cartoon-style-logo-concept-50-percent-sale-isolated-icon-on-black-background-fifty-percent-discount-for-business-illustration-vector.jpg"
            else -> "https://cdn.shopify.com/s/files/1/0817/7988/4088/articles/4XOfcVjU6L9Z0yxkgW0WeI_9a7fdb9d-4173-4023-816b-8918cc91229f.jpg?v=1712946016"
        }
        return SlideModel(img, "Click To Apply ${coupon.value} %")
    }

    private fun addImgFixedAmount(coupon: PriceRule): SlideModel {
        val img = "https://cdn.shopify.com/s/files/1/0817/7988/4088/articles/4XOfcVjU6L9Z0yxkgW0WeI_9a7fdb9d-4173-4023-816b-8918cc91229f.jpg?v=1712946016"
        return SlideModel(img, "Click To Apply ${coupon.value}")
    }

   /* private fun getCouponImages(coupons:List<PriceRule>)
    {
        for (item in coupons)
        {
            couponsIDs.add(item.id.toString())
            if (item.valueType == "percentage")
            {
                couponsList.add(addImgPercentage(item))
            }else
            {
                couponsList.add(addImgFixedAmount(item))
            }
        }
    }

    *//*private fun getCoupon()
    {
        delCoupons()
        couponsList.clear()
        couponsIDs.clear()
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCoupons().collect{
                getCouponImages(it.priceRules)
                saveCouponsLocally(it.priceRules)
            }
        }
    }*//*

    private fun getCoupon() {
        delCoupons()
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCoupons().collect { coupons ->
                viewModelScope.launch(Dispatchers.Main) {
                    couponsList.clear()
                    couponsIDs.clear()
                    getCouponImages(coupons.priceRules)
                    saveCouponsLocally(coupons.priceRules)
                    _couponsList.postValue(couponsList) // Notify observers
                }
            }
        }
    }


    private fun delCoupons()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllCoupons()
        }
    }
    private fun saveCouponsLocally(coupons:List<PriceRule>)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAllCoupons(coupons)
        }
    }

    private fun addImgPercentage(coupon: PriceRule):SlideModel {
        var img = when(coupon.value)
        {
            "-20.0"-> "https://t3.ftcdn.net/jpg/03/36/91/14/360_F_336911489_vQzdGPLdY0aNYXdu5rK7UIwwiEksYJgK.jpg"
            "-30.0"-> "https://static.vecteezy.com/system/resources/previews/004/141/832/non_2x/golden-30-percent-off-flat-cartoon-style-logo-concept-30-percent-sale-isolated-icon-on-black-background-thirty-percent-discount-for-business-illustration-vector.jpg"
            "-50.0"-> "https://static.vecteezy.com/system/resources/thumbnails/004/141/666/small_2x/golden-50-percent-off-flat-cartoon-style-logo-concept-50-percent-sale-isolated-icon-on-black-background-fifty-percent-discount-for-business-illustration-vector.jpg"
            else->"https://cdn.shopify.com/s/files/1/0817/7988/4088/articles/4XOfcVjU6L9Z0yxkgW0WeI_9a7fdb9d-4173-4023-816b-8918cc91229f.jpg?v=1712946016"
        }
        return SlideModel(img, "Click To Apply ${coupon.value} %")
    }
    private fun addImgFixedAmount(coupon: PriceRule):SlideModel
    {
        var img = "https://cdn.shopify.com/s/files/1/0817/7988/4088/articles/4XOfcVjU6L9Z0yxkgW0WeI_9a7fdb9d-4173-4023-816b-8918cc91229f.jpg?v=1712946016"
        return SlideModel(img, "Click To Apply ${coupon.value}")
    }*/

}