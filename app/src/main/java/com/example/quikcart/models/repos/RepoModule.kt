package com.example.quikcart.models.repos

import com.example.quikcart.models.local.LocalDataSource
import com.example.quikcart.models.local.LocalDataSourceInterface
import com.example.quikcart.models.remote.RemoteDataSource
import com.example.quikcart.models.remote.RemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun provideRemoteBrandRepo(remoteDataSourceImp: RemoteDataSourceImp): RemoteDataSource
    @Binds
    abstract fun provideBrandsRepo(brandRepoImp: RepositoryImp):Repository
    @Binds
    abstract fun provideLocalDataSource(localDataSource: LocalDataSource): LocalDataSourceInterface
}