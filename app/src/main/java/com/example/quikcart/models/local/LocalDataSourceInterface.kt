package com.example.quikcart.models.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import kotlinx.coroutines.flow.Flow


interface LocalDataSourceInterface
{
    suspend fun insertProducts(products: ProductsItem)
    suspend fun deleteProducts(products: ProductsItem)
    fun getAllProducts(): Flow<List<ProductsItem>>

    suspend fun deleteAllProducts()
    suspend fun insertCoupon(coupon: PriceRule): Long
    suspend fun deleteAllCoupons(): Int
    suspend fun insertAllCoupons(coupons: List<PriceRule>): LongArray
    suspend fun getAllCoupons(): Flow<List<PriceRule>>
}