package com.klt.data.repository

import com.klt.data.di.UnsplashIo
import com.klt.data.model.UnsplashPhotoDTO
import com.klt.data.remote.UnsplashService
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