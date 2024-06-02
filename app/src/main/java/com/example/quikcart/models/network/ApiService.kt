package com.example.quikcart.models.network

import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.utils.Constants
import com.example.quikcart.models.entities.BrandsResponse
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.FetchAddress
import com.example.quikcart.models.entities.OrderResponse
import com.example.quikcart.models.entities.PostAddressModel
import com.example.quikcart.models.entities.ProductsResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

import retrofit2.http.Path
import retrofit2.http.Url

import retrofit2.http.Query


interface ApiService {
    @GET("smart_collections.json?since_id=482865238&access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getBrands():BrandsResponse

    @GET("products.json?access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getProductsByBrandId(@Query("collection_id") brandId:Long): ProductsResponse

    @GET("products.json?access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getProductsBySubCategory(@Query("product_type") subCategory: String):ProductsResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
    @POST("customers.json")
    suspend fun postCustomer(
        @Body customerRequest: CustomerRequest
    ): Response<CustomerResponse>


    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.PASSWORD}")
    @POST("customers/{customerID}/addresses.json")
    suspend fun postAddress(
        @Path("customerID") customerID:Long,
        @Body address: PostAddressModel
    ): Response<FetchAddress>

    @Headers("X-Shopify-Access-Token:${Constants.PASSWORD}")
    @GET("customers/{customerID}/addresses.json")
    suspend fun getCustomerAddresses(@Path("customerID") customerID:Long): Response<AddressesResponse>

    @Headers("X-Shopify-Access-Token:${Constants.PASSWORD}")
    @DELETE("customers/{customerID}/addresses/{id}.json")
    suspend fun delCustomerAddress(@Path("customerID") customerID:Long,@Path("id")id : Long)

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+ Constants.ACCESS_TOKEN)
    @GET("products.json")
    suspend fun getProducts(): ProductsResponse

    @Headers("X-Shopify-Access-Token:"+ Constants.ACCESS_TOKEN)
    @GET("customers.json")
    suspend fun getCustomers(): CustomerResponse


    @GET("customers/{customer_id}/orders.json?access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getCustomerOrders(@Path("customer_id") customerID: Long):OrderResponse

}


