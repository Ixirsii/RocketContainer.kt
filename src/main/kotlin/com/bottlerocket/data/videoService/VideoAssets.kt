package com.bottlerocket.data.videoService

import kotlinx.serialization.Serializable

/**
 * Wrapper for a list of [AssetReference].
 */
@Serializable
data class VideoAssets(
    /** List of assets. */
    val videoAssets: List<AssetReference>
)
