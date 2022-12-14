package com.klt.unsplash_service.di

import com.klt.unsplash.data.BuildConfig
import com.klt.unsplash_service.remote.UnsplashService
import com.klt.unsplash_service.repository.UnsplashApiRepository
import com.klt.unsplash_service.repository.UnsplashApiRepositoryImpl
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
    fun providesOkhttp(): OkHttpClient =
        if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addNetworkInterceptor(OkHttpProfilerInterceptor())
                .addInterceptor { chain ->
                    chain.request().newBuilder()
                        .addHeader(
                            UnsplashService.AUTHORIZATION,
                            "Client-ID pehAInc1M37NqAQ8l8Zx2pH8RjG44qGNsAC_t-qONRA"
                        )//please don't reverse engineering, IDK how to hide this key in multimodule app. :C
                        .build()
                        .let(chain::proceed)
                }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }else  OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.request().newBuilder()
                    .addHeader(
                        UnsplashService.AUTHORIZATION,
                        "Client-ID pehAInc1M37NqAQ8l8Zx2pH8RjG44qGNsAC_t-qONRA"
                    )//please don't reverse engineering, IDK how to hide this key in multimodule app. :C
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