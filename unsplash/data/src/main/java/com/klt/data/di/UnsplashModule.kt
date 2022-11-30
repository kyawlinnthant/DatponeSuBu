package com.klt.data.di

import com.klt.data.remote.UnsplashService
import com.klt.data.repository.UnsplashApiRepository
import com.klt.data.repository.UnsplashApiRepositoryImpl
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UnsplashModule {
    @Provides
    @Singleton
    @UnsplashOkHttpClient
    fun providesOkhttp(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(OkHttpProfilerInterceptor())
        .addInterceptor { chain ->
            chain.request().newBuilder()
                .addHeader(UnsplashService.AUTHORIZATION, "O_AUTH_AUTHENTICATION")
                .build()
                .let(chain::proceed)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @UnsplashRetrofit
    fun providesRetrofit(
        @UnsplashOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UnsplashService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesService(
        @UnsplashRetrofit retrofit: Retrofit
    ): UnsplashService = retrofit.create(UnsplashService::class.java)


    @Provides
    @UnsplashIo
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
interface UnsplashRepositoryModule {

    @Binds
    @Singleton
    fun bindsApiRepository(repo: UnsplashApiRepositoryImpl): UnsplashApiRepository
}

//retrofit
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class UnsplashRetrofit

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class UnsplashOkHttpClient

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class UnsplashIo