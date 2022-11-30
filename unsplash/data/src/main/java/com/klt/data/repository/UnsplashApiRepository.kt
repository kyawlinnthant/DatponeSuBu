package com.klt.data.repository

import com.klt.data.model.UnsplashPhotoDTO
import com.klt.util.Result
import kotlinx.coroutines.flow.Flow

interface UnsplashApiRepository {

    suspend fun getPhotos(pageNumber: Int): Flow<Result<List<UnsplashPhotoDTO>>>
}