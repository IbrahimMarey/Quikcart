package com.example.quikcart.models.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LocalDataSourceTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        localDataSource = LocalDataSource(database.appDao())
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
    fun insertProducts_retrievesProduct() = runBlockingTest {

        // When
        localDataSource.insertProducts(product1)

        // Then
        val result = localDataSource.getAllProducts().first()
        assertThat(result[0].id, `is`(product1.id))
    }

    @Test
    fun deleteProducts_removesProduct() = runBlockingTest {
        localDataSource.insertProducts(product1)
        // When
        localDataSource.deleteProducts(product1)
        // Then
        val result = localDataSource.getAllProducts().first()
        assertThat(result.isEmpty(), `is`(true))
    }
    @Test
    fun getAllProducts_returnsAllProducts() = runBlockingTest {
        localDataSource.insertProducts(product1)
        localDataSource.insertProducts(product2)

        // When
        val result = localDataSource.getAllProducts().first()

        // Then
        assertThat(result.size, `is`(2))
        assertThat(result[0].id, `is`(product1.id))
        assertThat(result[1].id, `is`(product2.id))
    }

    @Test
    fun deleteAllProducts_removesAllProducts() = runBlockingTest {

        localDataSource.insertProducts(product1)
        // When
        localDataSource.deleteAllProducts()
        // Then
        val result = localDataSource.getAllProducts().first()
        assertThat(result.isEmpty(), `is`(true))
    }
    @Test
    fun insertCoupon_retrievesCoupon() = runBlockingTest {

        // When
        localDataSource.insertCoupon(coupon1)
        // Then
        val result = localDataSource.getAllCoupons().first()
        assertThat(result[0].id, `is`(coupon1.id))
    }

    @Test
    fun deleteAllCoupons_removesAllCoupons() = runBlockingTest {

        localDataSource.insertCoupon(coupon1)

        // When
        localDataSource.deleteAllCoupons()

        // Then
        val result = localDataSource.getAllCoupons().first()
        assertThat(result.isEmpty(), `is`(true))
    }
    @Test
    fun insertAllCoupons_retrievesAllCoupons() = runBlockingTest {

        val coupons = listOf(coupon1, coupon2)
        // When
        localDataSource.insertAllCoupons(coupons)
        // Then
        val result = localDataSource.getAllCoupons().first()
        assertThat(result.size, `is`(2))
        assertThat(result[0].id, `is`(coupon1.id))
        assertThat(result[1].id, `is`(coupon2.id))
    }

}