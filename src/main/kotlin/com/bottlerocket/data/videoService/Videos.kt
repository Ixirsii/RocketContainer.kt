package com.bottlerocket.data.videoService

import kotlinx.serialization.Serializable

/**
 * Wrapper for a list of [Video].
 */
@Serializable
data class Videos(
    /** List of videos. */
    val videos: List<Video>
)
