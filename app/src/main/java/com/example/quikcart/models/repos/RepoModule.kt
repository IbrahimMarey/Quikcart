package com.example.quikcart.models.repos

import com.example.quikcart.models.local.LocalDataSource
import com.example.quikcart.models.local.LocalDataSourceInterface
import com.example.quikcart.models.repos.appRepo.AppRepo
import com.example.quikcart.models.repos.appRepo.AppRepoInterface
import com.example.quikcart.models.repos.remote.brands.RemoteBrands
import com.example.quikcart.models.repos.remote.brands.RemoteBrandsImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun provideRemoteBrandRepo(remoteBrandsImp: RemoteBrandsImp):RemoteBrands
    @Binds
    abstract fun provideBrandsRepo(brandRepoImp: BrandRepoImp):BrandRepo

    @Binds
    abstract fun provideLocalDataSource(localDataSource: LocalDataSource): LocalDataSourceInterface
    @Binds
    abstract fun provideAppRepo(appRepo: AppRepo): AppRepoInterface
}