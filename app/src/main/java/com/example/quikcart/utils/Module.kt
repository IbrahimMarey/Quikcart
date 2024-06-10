package com.example.quikcart.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.quikcart.utils.PreferencesUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Module {
    @Provides
    fun provideSharedPreference(@ApplicationContext context:Context):PreferencesUtils{
        return PreferencesUtils(context)
    }
}