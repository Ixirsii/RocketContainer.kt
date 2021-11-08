package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

/**
 * Asset Reference data class.
 */
@Serializable
data class AssetReference(
    /** Unique identifier for referenced asset. */
    val assetId: Int,
    /** Type of asset. */
    val assetType: Type
) {
    /**
     * Type of asset.
     */
    enum class Type {
        /** Advertisement. */
        AD,
        /** Image. */
        IMAGE
    }
}
