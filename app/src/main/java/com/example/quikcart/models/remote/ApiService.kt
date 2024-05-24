package com.example.quikcart.models.remote

import com.example.quikcart.helpers.Constants
import com.example.quikcart.models.entities.BrandsResponse
import com.example.quikcart.models.entities.SmartCollectionsItem
import retrofit2.http.GET

interface ApiService {
    @GET("smart_collections.json?since_id=482865238&access_token=${Constants.ACCESS_TOKEN}")
    suspend fun getBrands():BrandsResponse
}