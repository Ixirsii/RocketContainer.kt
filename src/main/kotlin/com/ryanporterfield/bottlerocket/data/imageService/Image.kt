package com.ryanporterfield.bottlerocket.data.imageService

import kotlinx.serialization.Serializable

/**
 * Image data from image service.
 */
@Serializable
data class Image(
    /** Parent identifier. */
    val containerId: Int,
    /** Unique image identifier. */
    val id: Int,
    /** Name of image. */
    val name: String,
    /** Image URL. */
    val url: String
)
