package com.example.quikcart.models.network

import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.utils.Constants
import com.example.quikcart.models.entities.BrandsResponse
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.FetchAddress
import com.example.quikcart.models.entities.PostAddressModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
    @GET("smart_collections.json?since_id=482865238&access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getBrands():BrandsResponse
    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.ACCESS_TOKEN}")
    @POST("customers.json")
    suspend fun postCustomer(
        @Body customerRequest: CustomerRequest
    ): Response<CustomerResponse>

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.PASSWORD}")
    @POST("customers/7406457553131/addresses.json")
    suspend fun postAddress(
        @Body address: PostAddressModel
    ): Response<FetchAddress>

    @Headers("X-Shopify-Access-Token:${Constants.PASSWORD}")
    @GET("customers/7406457553131/addresses.json")
    suspend fun getCustomerAddresses(): Response<AddressesResponse>

    @Headers("X-Shopify-Access-Token:${Constants.PASSWORD}")
    @DELETE("customers/7406457553131/addresses/{id}.json")
    suspend fun delCustomerAddress(@Path("id")id : Long)
}


