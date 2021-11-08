package com.ryanporterfield.bottlerocket.data.imageService

import kotlinx.serialization.Serializable

/**
 * Wrapper for a list of [Image].
 */
@Serializable
data class Images(
    /** List of images. */
    val images: List<Image>
)
