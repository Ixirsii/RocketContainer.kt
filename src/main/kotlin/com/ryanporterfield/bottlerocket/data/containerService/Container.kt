package com.ryanporterfield.bottlerocket.data.containerService

import kotlinx.serialization.Serializable

/**
 * Container data class.
 */
@Serializable
data class Container(
    /** List of ads. */
    val ads: List<Advertisement>,
    /** Unique container identifier. */
    val id: Int,
    /** List of images. */
    val images: List<Image>,
    /** Title. */
    val title: String,
    /** List of videos. */
    val videos: List<Video>
)
