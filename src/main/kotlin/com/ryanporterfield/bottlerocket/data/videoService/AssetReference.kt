package com.ryanporterfield.bottlerocket.data.videoService

import com.ryanporterfield.bottlerocket.data.AssetType
import kotlinx.serialization.Serializable

/**
 * Asset reference data from video service.
 */
@Serializable
data class AssetReference(
    /** Unique identifier for referenced asset. */
    val assetId: Int,
    /** Type of asset. */
    val assetType: AssetType,
    /** Unique identifier for referenced video. */
    val videoId: Int
)
