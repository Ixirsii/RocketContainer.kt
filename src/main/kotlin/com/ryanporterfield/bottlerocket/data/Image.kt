package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

/**
 * Image data class.
 */
@Serializable
data class Image(
    /** Unique image identifier. */
    val id: Int,
    /** Name of image. */
    val name: String,
    /** Image URL. */
    val url: String
)
