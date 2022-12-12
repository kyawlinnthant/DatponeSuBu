package com.klt.pexels_service.remote

import com.klt.pexels_service.model.PexelsPhotosDTO
import com.klt.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsService {
    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BASE_URL = "https://api.pexels.com/"
        private const val VERSION = "v1/"
        private const val PHOTOS = VERSION + "curated"
    }
    @GET(value = PHOTOS)
    suspend fun getPhotos(
        @Query(value = "page") pageNumber: Int,//default = 1
        @Query(value = "per_page") photosPerPage: Int? = Constants.LOAD_SIZE, //default = 10, max = 80
    ): Response<PexelsPhotosDTO>
}