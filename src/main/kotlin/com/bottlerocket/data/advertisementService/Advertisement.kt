package com.bottlerocket.data.advertisementService

import kotlinx.serialization.Serializable

/**
 * Advertisement data from advertisement service.
 */
@Serializable
data class Advertisement(
    /** Parent identifier. */
    val containerId: Int,
    /** Unique advertisement identifier. */
    val id: Int,
    /** Name of advertisement. */
    val name: String,
    /** Ad playback URL. */
    val url: String
)
