package com.example.quikcart.models.repos.remote.brands

import com.example.quikcart.models.entities.SmartCollectionsItem
import kotlinx.coroutines.flow.Flow

interface RemoteBrands {
     fun getBrands():Flow<List<SmartCollectionsItem>>
}