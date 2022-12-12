package com.klt.pexels_service.model

import com.google.gson.annotations.SerializedName

data class PexelsPhotoDTO(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    @SerializedName("avg_color") val color: String,
    val src: Src,
    @SerializedName("photographer_id") val photographerId: Long,
    @SerializedName("photographer") val photographerName: String,
    @SerializedName("photographer_url") val photographerUrl: String,
    val liked: Boolean,
    @SerializedName("alt") val description: String
)