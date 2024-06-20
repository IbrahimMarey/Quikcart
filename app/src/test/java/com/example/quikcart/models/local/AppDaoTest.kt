package com.example.quikcart.models.local


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AppDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }
    val product1 = ProductsItem(
        id = 1,
        bodyHtml = "Test HTML",
        image = null,
        images = emptyList(),
        createdAt = "2021-01-01",
        handle = "test-handle",
        variants = emptyList(),
        title = "Test Product",
        tags = "tag1,tag2",
        publishedScope = "global",
        productType = "test type",
        templateSuffix = null,
        updatedAt = "2024-6-20",
        vendor = "Test Vendor",
        adminGraphqlApiId = "test-api-id",
        options = emptyList(),
        publishedAt = "2024-06-20",
        status = "active",
        price = "10.00",
        isFavorited = false
    )
    val product2 = ProductsItem(
        id = 2,
        bodyHtml = "Test HTML",
        image = null,
        images = emptyList(),
        createdAt = "2021-01-01",
        handle = "test-handle",
        variants = emptyList(),
        title = "Test Product",
        tags = "tag1,tag2",
        publishedScope = "global",
        productType = "test type",
        templateSuffix = null,
        updatedAt = "2024-6-20",
        vendor = "Test Vendor",
        adminGraphqlApiId = "test-api-id",
        options = emptyList(),
        publishedAt = "2024-06-20",
        status = "active",
        price = "10.00",
        isFavorited = false
    )
    val coupon1 = PriceRule(
        id = 1,
        valueType = "Coupon 1",
        value = "50.0"
    )
    val coupon2 = PriceRule(
        id = 2,
        valueType = "Coupon 2",
        value = "50.0"
    )

    @Test
    fun insertProduct_retrievesProduct() = runBlockingTest {
        // Given
        // When
        database.appDao().insertProduct(product1)
        // Then
        val result = database.appDao().getAllProducts().first()
        assertThat(result[0].id, `is`(product1.id))
    }
    @Test
    fun deleteProduct_removesProduct() = runBlockingTest {

        // When
        database.appDao().insertProduct(product1)
        database.appDao().deleteProduct(product1)
        // Then
        val result = database.appDao().getAllProducts().first()
        assertThat(result.isEmpty(), `is`(true))
    }
    @Test
    fun deleteAllProducts_removesAllProducts() = runBlockingTest {

        // When
        database.appDao().insertProduct(product1)
        database.appDao().insertProduct(product2)
        database.appDao().deleteAllProducts()
        // Then
        val result = database.appDao().getAllProducts().first()
        assertThat(result.isEmpty(), `is`(true))
    }
    @Test
    fun insertCoupon_retrievesCoupon() = runBlockingTest {

        // When
        database.appDao().insertCoupon(coupon1)
        // Then
        val result = database.appDao().getAllCoupons()
        assertThat(result[0].id, `is`(coupon1.id))
    }
    @Test
    fun deleteAllCoupons_removesAllCoupons() = runBlockingTest {

        // When
        database.appDao().insertCoupon(coupon1)
        database.appDao().insertCoupon(coupon2)
        database.appDao().deleteAllCoupons()
        // Then
        val result = database.appDao().getAllCoupons()
        assertThat(result.isEmpty(), `is`(true))
    }
    @Test
    fun insertAllCoupons_retrievesAllCoupons() = runBlockingTest {

        // When
        database.appDao().insertAllCoupons(listOf(coupon1, coupon2))
        // Then
        val result = database.appDao().getAllCoupons()
        assertThat(result.size, `is`(2))
        assertThat(result[0].id, `is`(coupon1.id))
        assertThat(result[1].id, `is`(coupon2.id))
    }

    @Test
    fun emptyDatabase_returnsEmptyLists() = runBlockingTest {
        // When
        val productsResult = database.appDao().getAllProducts().first()
        val couponsResult = database.appDao().getAllCoupons()
        // Then
        assertThat(productsResult.isEmpty(), `is`(true))
        assertThat(couponsResult.isEmpty(), `is`(true))
    }
}