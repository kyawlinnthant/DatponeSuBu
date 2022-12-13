package com.klt.unsplash_service.remote

import com.klt.unsplash_service.model.UnsplashErrorDTO
import com.klt.unsplash_service.model.UnsplashPhotoDTO
import com.klt.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashService {

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BASE_URL = "https://api.unsplash.com/"
        const val PHOTOS = "photos"
    }

    @GET(value = PHOTOS)
    suspend fun getPhotos(
        @Query(value = "page") pageNumber: Int,//default = 1
        @Query(value = "per_page") photosPerPage: Int? = Constants.LOAD_SIZE, //default = 10, max = 80
        @Query(value = "order_by") orderBy: String? = Constants.LOAD_SEQUENCE //default = "latest" : valid = latest, oldest, popular
    ): Response<List<UnsplashPhotoDTO>>

    @GET(value = PHOTOS)
    suspend fun getPhotosError(
        @Query(value = "page") pageNumber: Int,//default = 1
        @Query(value = "per_page") photosPerPage: Int? = Constants.LOAD_SIZE, //default = 10, max = 80
        @Query(value = "order_by") orderBy: String? = Constants.LOAD_SEQUENCE //default = "latest" : valid = latest, oldest, popular
    ): Response<UnsplashErrorDTO>
}