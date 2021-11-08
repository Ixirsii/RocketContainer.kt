package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

@Serializable
data class AssetReference(val assetId: Int, val assetType: Type) {
    enum class Type {
        AD, IMAGE
    }
}
