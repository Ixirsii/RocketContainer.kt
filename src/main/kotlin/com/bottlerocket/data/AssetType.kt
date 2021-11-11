package com.bottlerocket.data

import kotlinx.serialization.Serializable

/**
 * Type of asset.
 */
@Serializable
enum class AssetType {
    /** Advertisement. */
    AD,
    /** Image. */
    IMAGE
}
