package com.klt.pexels_service.repository

import com.klt.pexels_service.di.PexelsIo
import com.klt.pexels_service.model.PexelsPhotosDTO
import com.klt.pexels_service.remote.PexelsService
import com.klt.util.Result
import com.klt.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PexelsApiRepositoryImpl @Inject constructor(
    private val service: PexelsService,
    @PexelsIo val io: CoroutineDispatcher,
) : PexelsApiRepository {
    override suspend fun getPhotos(pageNumber: Int): Flow<Result<PexelsPhotosDTO>> {
        val response = safeApiCall {
            service.getPhotos(
                pageNumber = pageNumber
            )
        }
        return flow {
            emit(value = response)
        }.catch { e ->
            emit(value = Result.Error(error = e.localizedMessage ?: "An error is occurred"))
        }.flowOn(context = io)
    }
}