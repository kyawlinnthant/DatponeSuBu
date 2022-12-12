package com.klt.datponesubu.data.di

import com.klt.datponesubu.data.repository.NetworkRepository
import com.klt.datponesubu.data.repository.NetworkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Io
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsRepository(repo: NetworkRepositoryImpl): NetworkRepository
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Io