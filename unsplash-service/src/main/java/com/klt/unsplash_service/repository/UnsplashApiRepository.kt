package com.klt.unsplash_service.repository

import com.klt.unsplash_service.model.UnsplashPhotoDTO
import com.klt.util.Result
import kotlinx.coroutines.flow.Flow

interface UnsplashApiRepository {

    suspend fun getPhotos(pageNumber: Int): Flow<Result<List<UnsplashPhotoDTO>>>
}