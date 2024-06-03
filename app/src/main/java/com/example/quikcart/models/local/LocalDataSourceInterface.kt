package com.example.quikcart.models.local

import com.example.quikcart.models.entities.ProductsItem
import kotlinx.coroutines.flow.Flow


interface LocalDataSourceInterface
{
    suspend fun insertProducts(products: ProductsItem)
    suspend fun deleteProducts(products: ProductsItem)
    fun getAllProducts(): Flow<List<ProductsItem>>
}