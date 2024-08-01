package com.example.lamadev.dependency_injection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationContextModule {

    @Provides
    @Singleton
    fun getContext(@ApplicationContext context: Context):Context{
        return context
    }
}