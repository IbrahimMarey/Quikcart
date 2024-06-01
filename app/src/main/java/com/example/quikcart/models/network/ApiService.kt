package com.example.quikcart.models.network

import com.example.quikcart.utils.Constants
import com.example.quikcart.models.entities.BrandsResponse
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.ProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("smart_collections.json?since_id=482865238&access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getBrands():BrandsResponse

    @GET("products.json?access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getProductsByBrandId(@Query("collection_id") brandId:Long):ProductsResponse

    @GET("products.json?access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getProductsBySubCategory(@Query("product_type") subCategory: String):ProductsResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
    @POST("customers.json")
    suspend fun postCustomer(
        @Body customerRequest: CustomerRequest
    ): Response<CustomerResponse>

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+ Constants.ACCESS_TOKEN)
    @GET("products.json")
    suspend fun getProducts(): ProductsResponse
}


