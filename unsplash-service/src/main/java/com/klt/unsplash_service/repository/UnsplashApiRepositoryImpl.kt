package com.klt.unsplash_service.repository

import com.klt.unsplash_service.di.UnsplashIo
import com.klt.unsplash_service.model.UnsplashPhotoDTO
import com.klt.unsplash_service.remote.UnsplashService
import com.klt.util.Result
import com.klt.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UnsplashApiRepositoryImpl @Inject constructor(
    private val service: UnsplashService,
    @UnsplashIo private val io: CoroutineDispatcher
) : UnsplashApiRepository {
    override suspend fun getPhotos(pageNumber: Int): Flow<Result<List<UnsplashPhotoDTO>>> {
        val response = safeApiCall {
            service.getPhotos(
                pageNumber = pageNumber
            )
        }
        return flow {
            emit(value = response)
        }.flowOn(context = io)
    }
}