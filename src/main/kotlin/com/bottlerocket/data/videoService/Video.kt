package com.bottlerocket.data.videoService

import com.bottlerocket.data.VideoType
import kotlinx.serialization.Serializable

/**
 * Video data from video service.
 */
@Serializable
data class Video(
    /** Parent container e.g. show/series identifier. */
    val containerId: Int,
    /** Brief description of the video. */
    val description: String,
    /** Expiration date for video in ISO-8601 format. */
    val expirationDate: String,
    /** Unique video identifier. */
    val id: Int,
    /** URL for video playback. */
    val playbackUrl: String,
    /** Video title. */
    val title: String,
    /** Type of video. */
    val type: VideoType
)
