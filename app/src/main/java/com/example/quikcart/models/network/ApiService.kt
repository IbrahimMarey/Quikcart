package com.example.quikcart.models.network

import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.utils.Constants
import com.example.quikcart.models.entities.BrandsResponse
import com.example.quikcart.models.entities.CouponModel
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.FetchAddress
import com.example.quikcart.models.entities.OrderResponse
import com.example.quikcart.models.entities.PostAddressModel
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.ProductsResponse
import com.example.quikcart.models.entities.Order
import com.example.quikcart.models.entities.cart.AllDraftOrdersResponse
import com.example.quikcart.models.entities.cart.DraftOrderResponse
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

import retrofit2.http.Path

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


    @GET("products/{product_id}.json?access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getProductById(@Path("product_id") productId:Long):ProductsItem

    @Headers("X-Shopify-Access-Token:"+ Constants.ACCESS_TOKEN)
    @GET("customers.json")
    suspend fun getCustomers(): CustomerResponse


    @GET("customers/{customer_id}/orders.json?access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getCustomerOrders(@Path("customer_id") customerID: Long):OrderResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.PASSWORD}")
    @POST("draft_orders.json")
    suspend fun postCartItem(
        @Body cartItem: PostDraftOrderItemModel
    ): Response<PostDraftOrderItemModel>

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.PASSWORD}")
    @GET("draft_orders.json")
    suspend fun getCartItems(): Response<AllDraftOrdersResponse>

    @Headers("X-Shopify-Access-Token:${Constants.PASSWORD}")
    @DELETE("draft_orders/{id}.json")
    suspend fun delCartItem(@Path("id") id : String)

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.PASSWORD}")
    @POST("draft_orders.json")
    suspend fun postDraftOrder(@Body draftOrderPostBody: PostDraftOrderItemModel) : DraftOrderResponse //Response<DraftOrderResponse>

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.PASSWORD}")
    @PUT("draft_orders/{id}.json")
    suspend fun putDraftOrder(@Path("id") id : String, @Body draftOrderPutBody: PutDraftOrderItemModel) : DraftOrderResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.PASSWORD}")
    @GET("draft_orders.json")
    suspend fun getAllDraftOrders() : AllDraftOrdersResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:${Constants.PASSWORD}")
    @GET("draft_orders/{id}.json")
    suspend fun getDraftOrderById(@Path("id") id : String) : DraftOrderResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token:"+Constants.PASSWORD)
    @GET("price_rules.json")
    suspend fun getCoupons(): CouponModel

    @POST("orders.json?access_token=${Constants.ACCESS_TOKEN}")
    suspend fun confirmOrder(@Body orderResponse:Order):Order


}


