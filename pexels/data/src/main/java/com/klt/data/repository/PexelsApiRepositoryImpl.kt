package com.klt.data.repository

import com.klt.core.Result
import com.klt.core.safeApiCall
import com.klt.data.di.PexelsIo
import com.klt.data.model.PhotoDTO
import com.klt.data.remote.PexelsService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PexelsApiRepositoryImpl @Inject constructor(
    private val service: PexelsService,
    @PexelsIo val io: CoroutineDispatcher,
) : PexelsApiRepository {
    override suspend fun getPhotos(pageNumber: Int): Flow<Result<PhotoDTO>> {
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