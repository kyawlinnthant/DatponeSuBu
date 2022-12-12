package com.klt.unsplash_service.remote

import com.klt.unsplash_service.model.UnsplashPhotoDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashService {

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BASE_URL = "https://api.unsplash.com/"
        private const val PHOTOS = "photos"
    }

    @GET(value = PHOTOS)
    suspend fun getPhotos(
        @Query(value = "page") pageNumber: Int,//default = 1
        @Query(value = "per_page") photosPerPage: Int? = 15, //default = 10, max = 80
        @Query(value = "order_by") orderBy: String? = "latest"
    ): Response<List<UnsplashPhotoDTO>>
}