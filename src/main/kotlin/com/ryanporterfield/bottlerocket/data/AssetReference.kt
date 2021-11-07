package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

@Serializable
data class AssetReference(val assetId: Int, val assetType: String)
