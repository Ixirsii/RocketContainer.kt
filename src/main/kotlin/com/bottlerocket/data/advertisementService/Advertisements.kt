package com.bottlerocket.data.advertisementService

import kotlinx.serialization.Serializable

/**
 * Wrapper for a list of [Advertisement].
 */
@Serializable
data class Advertisements(
    /** List of advertisements. */
    val advertisements: List<Advertisement>
)
