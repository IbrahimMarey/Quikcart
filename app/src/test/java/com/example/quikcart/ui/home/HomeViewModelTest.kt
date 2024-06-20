package com.example.quikcart.ui.home

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.denzcoskun.imageslider.models.SlideModel
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.SmartCollectionsItem

import com.example.quikcart.models.repos.Repository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import kotlin.test.Test
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var repository: FakeRepo

    @Before
    fun setUp() {
        repository = FakeRepo()
        homeViewModel = HomeViewModel(repository)


    }

    @Test
    fun getCategoriesShouldReturnListOfCategories(){
        val categories=homeViewModel.getCategories()
        assertEquals(repository.getCategories(),categories)

    }

    @Test
    fun getCouponsShouldReturnListOfCoupons() = runTest {
        homeViewModel.getCoupon()
        advanceUntilIdle()

        val expectedCoupons = repository.coupons.priceRules.map {
            SlideModel(
                "https://cdn.shopify.com/s/files/1/0817/7988/4088/articles/4XOfcVjU6L9Z0yxkgW0WeI_9a7fdb9d-4173-4023-816b-8918cc91229f.jpg?v=1712946016",
                "Click To Apply ${it.value}"
            )
        }
        assertEquals(expectedCoupons, homeViewModel.couponsList.value)
    }

    @Test
    fun getBrandsShouldSetUiStateToSuccess() = runTest {
        homeViewModel.getBrands()
        advanceUntilIdle()

        val uiState = homeViewModel.uiState.value
        assertTrue(uiState is ViewState.Success)
        assertEquals(repository.smartCollections, uiState.data)
    }

    @Test
    fun testAddImgPercentage() {
        val coupon = PriceRule(value = "-20.0", valueType = "percentage", id = 0)
        val expectedImg = "https://t3.ftcdn.net/jpg/03/36/91/14/360_F_336911489_vQzdGPLdY0aNYXdu5rK7UIwwiEksYJgK.jpg"
        val result = homeViewModel.addImgPercentage(coupon)
        assertEquals(expectedImg, result.imageUrl)
    }

    @Test
    fun testAddImgFixedAmount() {
        val coupon = PriceRule(value = "10.0", valueType = "fixed_amount", id = 1)
        val expectedImg = "https://cdn.shopify.com/s/files/1/0817/7988/4088/articles/4XOfcVjU6L9Z0yxkgW0WeI_9a7fdb9d-4173-4023-816b-8918cc91229f.jpg?v=1712946016"
        val result = homeViewModel.addImgFixedAmount(coupon)
        assertEquals(expectedImg, result.imageUrl)
    }

    @Test
    fun testGetCouponImages() {
        val coupons = listOf(
            PriceRule(id = 1, value = "-20.0", valueType = "percentage"),
            PriceRule(id = 2, value = "10.0", valueType = "fixed_amount")
        )
        val newCouponsList = mutableListOf<SlideModel>()
        homeViewModel.getCouponImages(coupons, newCouponsList)
        assertEquals(2, newCouponsList.size)
        assertEquals("https://t3.ftcdn.net/jpg/03/36/91/14/360_F_336911489_vQzdGPLdY0aNYXdu5rK7UIwwiEksYJgK.jpg", newCouponsList[0].imageUrl)
        assertEquals("https://cdn.shopify.com/s/files/1/0817/7988/4088/articles/4XOfcVjU6L9Z0yxkgW0WeI_9a7fdb9d-4173-4023-816b-8918cc91229f.jpg?v=1712946016", newCouponsList[1].imageUrl)
    }

    @Test
    fun testSaveCouponsLocally() = runTest {
        val coupons = listOf(
            PriceRule(id = 1, value = "-20.0", valueType = "percentage"),
            PriceRule(id = 2, value = "10.0", valueType = "fixed_amount")
        )
        homeViewModel.saveCouponsLocally(coupons)
        advanceUntilIdle()
        assertEquals(coupons, repository.coupons.priceRules)
    }


}


