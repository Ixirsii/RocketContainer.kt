package com.ryanporterfield.bottlerocket.data.containerService

import com.ryanporterfield.bottlerocket.data.AssetType
import kotlinx.serialization.Serializable

/**
 * Asset Reference data class.
 */
@Serializable
data class AssetReference(
    /** Unique identifier for referenced asset. */
    val assetId: Int,
    /** Type of asset. */
    val assetType: AssetType
)
