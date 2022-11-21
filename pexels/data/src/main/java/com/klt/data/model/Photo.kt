package com.klt.data.model

import com.google.gson.annotations.SerializedName

data class Photo(
    val id: Long,
    val url: String,
    @SerializedName("avg_color")
    val color: String,
    val width: Int,
    val height: Int,
    val src: Src,

    @SerializedName("photographer_id")
    val photographerId: Long,
    @SerializedName("photographer")
    val photographerName: String,
    @SerializedName("photographer_url")
    val photographerUrl: String,

    val liked: Boolean,
    val alt: String
) {
    fun toVo(): PhotoVo = PhotoVo(
        id = id,
        url = url,
        color = color,
        width = width,
        height = height,
        photographer = PhotographerVo(
            id = photographerId,
            url = photographerUrl,
            name = photographerName,
        ),
        srcVo = src.toVo()
    )
}

data class PhotoVo(
    val id: Long,
    val url: String,
    val color: String,
    val width: Int,
    val height: Int,
    val photographer: PhotographerVo,
    val srcVo: SrcVo,
)

data class PhotographerVo(
    val id: Long,
    val name: String,
    val url: String,
)