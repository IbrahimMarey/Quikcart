package com.example.quikcart.models.repos.remote.brands

import com.example.quikcart.models.entities.BrandsResponse
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteBrandsImp @Inject constructor(private val apiService: ApiService):RemoteBrands {
    override fun getBrands(): Flow<List<SmartCollectionsItem>> = flow {
        val response = apiService.getBrands().smartCollections
        response?.filterNotNull()?.let { emit(it) }
    }


}