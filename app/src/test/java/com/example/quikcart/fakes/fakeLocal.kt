package com.example.quikcart.fakes

import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.local.LocalDataSourceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource:LocalDataSourceInterface{
    override suspend fun insertProducts(products: ProductsItem) {

    }

    override suspend fun deleteProducts(products: ProductsItem) {
    }

    override fun getAllProducts(): Flow<List<ProductsItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllProducts() {
        TODO("Not yet implemented")
    }

    override suspend fun insertCoupon(coupon: PriceRule): Long {
        return 1234567
    }

    override suspend fun deleteAllCoupons(): Int {
        return 1
    }

    override suspend fun insertAllCoupons(coupons: List<PriceRule>): LongArray {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCoupons(): Flow<List<PriceRule>> {
        return flow {
            try {
                val priceRules = getPriceRulesFromLocalSource()

                emit(priceRules)
            } catch (e: Exception) {
                // Handle any exceptions that occur during the data retrieval
                throw Exception("Failed to retrieve price rules: ${e.message}")
            }
        }
    }

    private fun getPriceRulesFromLocalSource(): List<PriceRule> {
        // Implement the logic to retrieve the price rules from a local data source
        // (e.g., a Room database, SharedPreferences, or any other local storage)
        return listOf(
            PriceRule(
                id = 1,
                valueType = "percentage",
                value = "10"
            ),
            PriceRule(
                id = 2,
                valueType = "fixed_amount",
                value = "5.99"
            ),
            PriceRule(
                id = 3,
                valueType = "buy_x_get_y",
                value = "2-1"
            )
        )
    }

}