package com.klt.data.model

data class Src(
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String
) {
    fun toVo(): SrcVo = SrcVo(
        original = original,
        tiny = tiny,
        small = small,
        medium = medium,
        large = large,
        large2x = large2x,
        landscape = landscape,
        portrait = portrait
    )
}

data class SrcVo(
    val original: String,
    val tiny: String,
    val small: String,
    val medium: String,
    val large: String,
    val large2x: String,
    val landscape: String,
    val portrait: String,
)