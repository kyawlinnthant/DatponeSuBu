package com.klt.datponesubu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.klt.datponesubu.data.di.Io
import com.klt.datponesubu.data.model.Photo
import com.klt.datponesubu.data.model.transform
import com.klt.pexels_service.remote.PexelsService
import com.klt.pexels_service.repository.PexelsApiRepository
import com.klt.unsplash_service.remote.UnsplashService
import com.klt.unsplash_service.repository.UnsplashApiRepository
import com.klt.util.Constants
import com.klt.util.Result
import com.klt.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val pexels: PexelsApiRepository,
    private val unsplash: UnsplashApiRepository,
    private val pexelsService: PexelsService,
    private val unsplashService: UnsplashService,
    @Io private val io: CoroutineDispatcher
) : NetworkRepository {

    private val config = PagingConfig(
        pageSize = Constants.LOAD_SIZE,
        enablePlaceholders = false
    )

    override suspend fun getPhotos(pageNumber: Int): Flow<Result<List<Photo>>> {
        val pexelsCall = pexels.getPhotos(pageNumber).map {
            when (it) {
                is Result.Error -> Result.Error(error = it.error)
                is Result.Success -> Result.Success(data = it.data.photos.map { dto ->
                    dto.transform()
                })
            }
        }
        val unsplashCall = unsplash.getPhotos(pageNumber).map {
            when (it) {
                is Result.Error -> Result.Error(error = it.error)
                is Result.Success -> Result.Success(data = it.data.map { dto ->
                    dto.transform()
                })
            }
        }

        val result = merge(pexelsCall, unsplashCall).catch { e ->
            Result.Error(
                error = e.localizedMessage ?: "Can't transform"
            )
        }.flowOn(context = io)

        return result
    }

    override suspend fun getPhotosPageable(): Flow<PagingData<Photo>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                PhotoPagingSource(
                    pexels = pexelsService,
                    unsplash = unsplashService
                )
            }
        ).flow.flowOn(context = io)
    }

    override suspend fun getPexels(): Flow<Result<List<Photo>>> {
        val request = safeApiCall {
            pexelsService.getPhotos(pageNumber = Constants.INITIAL_PAGE)
        }
        val response = when (request) {
            is Result.Error -> Result.Error(error = request.error)
            is Result.Success -> Result.Success(data = request.data.photos.map { it.transform() })
        }
        return flow {
            emit(value = response)
        }.flowOn(context = io)
    }
}