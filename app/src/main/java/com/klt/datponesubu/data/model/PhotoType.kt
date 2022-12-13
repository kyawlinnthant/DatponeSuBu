package com.klt.datponesubu.data.model

sealed interface PhotoType{
    object Unsplash : PhotoType
    object Pexels : PhotoType
}