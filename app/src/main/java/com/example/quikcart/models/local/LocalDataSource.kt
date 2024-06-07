package com.example.quikcart.models.local

import com.example.quikcart.models.entities.ProductsItem
import kotlinx.coroutines.flow.Flow
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

}