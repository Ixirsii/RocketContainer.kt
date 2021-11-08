package com.ryanporterfield.bottlerocket.data

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
