package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

/**
 * Video data class.
 */
@Serializable
data class Video(
    /** Ad and Image assets associated with the video. */
    val assets: List<AssetReference>,
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
    val type: Type
) {
    /**
     * Video type.
     */
    enum class Type {
        /** Clip/short. */
        CLIP,
        /** TV length episode. */
        EPISODE,
        /** Full length movie. */
        MOVIE
    }
}
