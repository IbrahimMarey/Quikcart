package com.example.quikcart.models.repos

import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.repos.remote.brands.RemoteBrands
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow


interface BrandRepo {
    fun getBrands(): Flow<List<SmartCollectionsItem>>

}

class BrandRepoImp @Inject constructor(private val brandRemoteRepo:RemoteBrands):BrandRepo{
    override fun getBrands(): Flow<List<SmartCollectionsItem>> {
        return brandRemoteRepo.getBrands()
    }


}