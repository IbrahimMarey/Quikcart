package com.example.quikcart.models.repos

import android.content.Context
import com.example.quikcart.models.local.AppDao
import com.example.quikcart.models.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepoModuleProvider {
    @Provides
    fun provideAppDao(@ApplicationContext ctx: Context): AppDao {
        return AppDatabase.getInstance(ctx).appDao()
    }

    /*@Binds
    abstract fun provideLocalDataSource(localDataSource: LocalDataSource):LocalDataSourceInterface
    @Binds
    abstract fun provideAppRepo(appRepo: AppRepo):AppRepoInterface
    */
}