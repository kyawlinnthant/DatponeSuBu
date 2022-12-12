package com.klt.datponesubu.data.repository

import com.klt.datponesubu.data.model.Photo
import com.klt.util.Result
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    suspend fun getPhotos(
        pageNumber: Int
    ): Flow<Result<List<Photo>>>
}