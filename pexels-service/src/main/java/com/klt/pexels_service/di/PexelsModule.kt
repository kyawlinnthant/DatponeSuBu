package com.klt.pexels_service.di

import com.klt.pexels_service.remote.PexelsService
import com.klt.pexels_service.repository.PexelsApiRepository
import com.klt.pexels_service.repository.PexelsApiRepositoryImpl
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PexelsDataModule {

    @Provides
    @Singleton
    @PexelsOkHttpClient
    fun providesOkhttp(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(OkHttpProfilerInterceptor())
        .addInterceptor { chain ->
            chain.request().newBuilder()
                .addHeader(PexelsService.AUTHORIZATION, "563492ad6f9170000100000150cf5003da2241dd99d1c9ab8fa28971")//please don't reverse engineering, IDK how to hide this key in multimodule app. :C
                .build()
                .let(chain::proceed)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @PexelsRetrofit
    fun providesRetrofit(
        @PexelsOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PexelsService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesPexelsService(
        @PexelsRetrofit retrofit: Retrofit
    ): PexelsService = retrofit.create(PexelsService::class.java)



    @Provides
    @PexelsIo
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
interface PexelsRepositoryModule {

    @Binds
    @Singleton
    fun bindsPexelsApiRepository(repo: PexelsApiRepositoryImpl): PexelsApiRepository
}

//retrofit
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class PexelsRetrofit

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class PexelsOkHttpClient

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class PexelsIo

