package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

@Serializable
data class Container(
    val ads: List<Advertisement>,
    val id: Int,
    val images: List<Image>,
    val title: String,
    val videos: List<Video>
)
