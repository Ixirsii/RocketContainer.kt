package com.ryanporterfield.bottlerocket.data.containerService

import kotlinx.serialization.Serializable

/**
 * Advertisement data class.
 */
@Serializable
data class Advertisement(
    /** Unique advertisement identifier. */
    val id: Int,
    /** Name of advertisement. */
    val name: String,
    /** Ad playback URL. */
    val url: String
)
