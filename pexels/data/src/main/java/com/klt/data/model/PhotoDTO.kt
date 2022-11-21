package com.klt.data.model

import com.google.gson.annotations.SerializedName

data class PhotoDTO(
    @SerializedName("next_page")
    val nextPage: String,
    val page: Int,
    @SerializedName("per_page")
    val photosPerPage: Int,
    val photos: List<Photo>,
    @SerializedName("total_results")
    val totalResults: Int
)