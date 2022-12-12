package com.klt.datponesubu.data.repository

import com.klt.datponesubu.data.di.Io
import com.klt.datponesubu.data.model.Photo
import com.klt.pexels_service.repository.PexelsApiRepository
import com.klt.unsplash_service.repository.UnsplashApiRepository
import com.klt.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val pexels: PexelsApiRepository,
    private val unsplash: UnsplashApiRepository,
    @Io private val io: CoroutineDispatcher
) : NetworkRepository {
    override suspend fun getPhotos(pageNumber: Int): Flow<Result<List<Photo>>> {
        val pexelsCall = pexels.getPhotos(pageNumber)
        val unsplashCall = unsplash.getPhotos(pageNumber)

        return emptyFlow()
    }
}