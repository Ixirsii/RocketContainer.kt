package com.bottlerocket.data

import kotlinx.serialization.Serializable

/**
 * Video type.
 */
@Serializable
enum class VideoType {
    /** Clip/short. */
    CLIP,
    /** TV length episode. */
    EPISODE,
    /** Full length movie. */
    MOVIE
}
