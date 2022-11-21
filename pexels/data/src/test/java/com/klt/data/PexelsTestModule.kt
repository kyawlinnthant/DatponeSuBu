package com.klt.data

import com.klt.data.remote.PexelsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PexelsTestModule {

    @Provides
    @Singleton
    @PexelsTestService
    fun provideMockPexelsApiService(): PexelsService {
        return Retrofit.Builder()
            .baseUrl(MockWebServer().url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsService::class.java)
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class PexelsTestService

}