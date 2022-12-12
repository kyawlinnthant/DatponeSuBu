package com.klt.pexels_service.model

import com.google.gson.annotations.SerializedName

data class PexelsPhotosDTO(

    @SerializedName("page") val pageNumber: Int,
    @SerializedName("per_page") val photosPerPage: Int,
    @SerializedName("next_page") val nextPage: String?,
    @SerializedName("total_results") val totalResults: Int,
    val photos: List<PexelsPhotoDTO>,
)