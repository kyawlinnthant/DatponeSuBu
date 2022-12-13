package com.klt.datponesubu.data.model

import com.klt.pexels_service.model.PexelsPhotoDTO
import com.klt.unsplash_service.model.UnsplashPhotoDTO

fun UnsplashPhotoDTO.transform(): Photo {
    return Photo(
        type = PhotoType.Unsplash,
        id = this.id,
        width = this.width.toLong(),
        height = this.height.toLong(),
        url = this.urls.full,
        description = this.description,
        color = this.color,
        resource = Resource(
            tiny = this.urls.small_s3,
            small = this.urls.small,
            medium = this.urls.regular,
            regular = this.urls.regular,
            large = this.urls.full
        )
    )
}

fun PexelsPhotoDTO.transform(): Photo {
    return Photo(
        type = PhotoType.Pexels,
        id = this.id.toString(),
        width = this.width.toLong(),
        height = this.height.toLong(),
        url = this.url,
        color = this.color,
        description = this.description,
        resource = Resource(
            tiny = this.src.tiny,
            small = this.src.small,
            medium = this.src.medium,
            regular = this.src.original,
            large = this.src.large
        )
    )
}