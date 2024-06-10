package com.example.quikcart.models.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductsItem)
    @Delete
    suspend fun deleteProduct(product: ProductsItem)
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductsItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: PriceRule): Long
    @Query("DELETE FROM coupon")
    suspend fun deleteAllCoupons(): Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCoupons(coupons: List<PriceRule>): LongArray
    @Query("SELECT * FROM coupon")
    suspend fun getAllCoupons(): List<PriceRule>


}