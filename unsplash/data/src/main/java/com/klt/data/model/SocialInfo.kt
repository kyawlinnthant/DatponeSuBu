package com.klt.data.model

import com.google.gson.annotations.SerializedName

data class SocialInfo(
    @SerializedName(value = "instagram_username") val instagramName : String?,
    @SerializedName(value = "twitter_username") val twitterName : String?,
    @SerializedName(value = "portfolio_url") val website : String?,

)
