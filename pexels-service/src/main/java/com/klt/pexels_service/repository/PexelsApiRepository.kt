package com.klt.pexels_service.repository

import com.klt.pexels_service.model.PexelsPhotosDTO
import com.klt.util.Result
import kotlinx.coroutines.flow.Flow

interface PexelsApiRepository {

    suspend fun getPhotos(pageNumber: Int): Flow<Result<PexelsPhotosDTO>>

}