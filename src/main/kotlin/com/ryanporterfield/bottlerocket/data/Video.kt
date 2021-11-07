package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val assets: List<AssetReference>,
    val description: String,
    val expirationDate: String,
    val id: Int,
    val playbackUrl: String,
    val title: String,
    val type: String
)
