package com.klt.datponesubu.data.model

data class Photo(
    val type : PhotoType,
    val id : String,
    val width : Long,
    val height : Long,
    val url : String,
    val color : String,
    val description : String,
    val resource: Resource,

)

