package com.klt.data.repository

import com.klt.data.model.PhotoDTO
import com.klt.util.Result
import kotlinx.coroutines.flow.Flow

interface PexelsApiRepository {

    suspend fun getPhotos(pageNumber: Int): Flow<Result<PhotoDTO>>

}