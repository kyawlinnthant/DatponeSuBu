package com.klt.datponesubu.data.di

import com.klt.datponesubu.data.repository.NetworkRepository
import com.klt.datponesubu.data.repository.NetworkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Io
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @RepoScope
    fun provideRepoScope(
        @Io ioDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)
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

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class RepoScope