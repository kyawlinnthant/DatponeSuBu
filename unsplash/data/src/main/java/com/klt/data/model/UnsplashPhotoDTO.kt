package com.klt.data.model

import com.google.gson.annotations.SerializedName

data class UnsplashPhotoDTO(
    val id: String,
    @SerializedName(value = "created_at") val createdDate: String,
    @SerializedName(value = "updated_at") val updatedDate: String,
    @SerializedName(value = "promoted_at") val promotedDate: String,
    val width: Int,
    val height: Int,
    val color: String,
    @SerializedName(value = "blur_hash") val blurHash: String,
    @SerializedName(value = "description") val shortDescription: String?,
    @SerializedName(value = "alt_description") val description: String,
    @SerializedName(value = "links") val ownerLinks: Links,
    val likes: Int,
    val urls: Urls,
    val user: User,
)