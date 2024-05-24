package com.example.quikcart.models.firebase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepoModule {
    @Binds
    abstract fun provideAuthRepo(authRepoImpl: AuthRepositoryImp): AuthenticationRepository
}