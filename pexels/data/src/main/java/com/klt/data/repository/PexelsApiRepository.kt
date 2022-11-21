package com.klt.data.repository

import com.klt.core.Result
import com.klt.data.model.PhotoDTO
import kotlinx.coroutines.flow.Flow

interface PexelsApiRepository {

    suspend fun getPhotos(pageNumber: Int): Flow<Result<PhotoDTO>>

}