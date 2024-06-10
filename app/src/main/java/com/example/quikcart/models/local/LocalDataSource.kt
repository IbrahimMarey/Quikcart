package com.example.quikcart.models.local

import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ProductsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val appDao: AppDao) :LocalDataSourceInterface {
    override suspend fun insertProducts(products: ProductsItem) {
        appDao.insertProduct(products)
    }
    override suspend fun deleteProducts(products: ProductsItem) {
        appDao.deleteProduct(products)
    }
    override fun getAllProducts(): Flow<List<ProductsItem>> {
        return appDao.getAllProducts()
    }

    override suspend fun insertCoupon(coupon: PriceRule): Long {
        return appDao.insertCoupon(coupon)
    }

    override suspend fun deleteAllCoupons(): Int {
        return appDao.deleteAllCoupons()
    }

    override suspend fun insertAllCoupons(coupons: List<PriceRule>): LongArray {
        return appDao.insertAllCoupons(coupons)
    }

    override suspend fun getAllCoupons(): Flow<List<PriceRule>> = flow{
         emit(appDao.getAllCoupons())
    }

}