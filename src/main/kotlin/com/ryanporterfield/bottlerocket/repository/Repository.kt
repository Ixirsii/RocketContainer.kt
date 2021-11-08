package com.ryanporterfield.bottlerocket.repository

import com.ryanporterfield.bottlerocket.data.Advertisement
import com.ryanporterfield.bottlerocket.data.AssetReference
import com.ryanporterfield.bottlerocket.data.Image
import com.ryanporterfield.bottlerocket.data.Video
import io.ktor.client.*
import io.ktor.client.request.*

/** Asset reference endpoint is in the format $VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX. */
const val ASSET_REFERENCE_SUFFIX = "asset-references"
/** URL endpoint for advertisement service. */
const val ADVERTISEMENT_ENDPOINT = "http://ads.rocket-stream.bottlerocketservices.com/advertisements"
/** URL endpoint for image service. */
const val IMAGE_ENDPOINT = "http://images.rocket-stream.bottlerocketservices.com/images"
/** URL endpoint for video service. */
const val VIDEO_ENDPOINT = "http://videos.rocket-stream.bottlerocketservices.com/videos"

/**
 * Get video by ID from video service.
 *
 * @param client HTTP client.
 * @param videoId ID of the video to get.
 */
suspend fun getVideo(client: HttpClient, videoId: Int): Video {
    return client.get("$VIDEO_ENDPOINT/$videoId")
}

/**
 * List all advertisements from advertisement service.
 *
 * @param client HTTP client.
 */
suspend fun listAdvertisements(client: HttpClient): List<Advertisement> {
    return client.get(ADVERTISEMENT_ENDPOINT)
}

/**
 * List advertisements, filtered by container ID, from advertisement service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 */
suspend fun listAdvertisements(client: HttpClient, containerId: Int): List<Advertisement> {
    return client.get(ADVERTISEMENT_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List all asset references for a video from video service.
 *
 * @param client HTTP client.
 * @param videoId ID of the video to get assets from.
 */
suspend fun listAssetReferences(client: HttpClient, videoId: Int): List<AssetReference> {
    return client.get("$VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX")
}

/**
 * List asset references, filtered by asset type, from video service.
 *
 * @param client HTTP client.
 * @param videoId ID of the video to get assets from.
 * @param assetType Asset type to filter by.
 */
suspend fun listAssetReferences(client: HttpClient, videoId: Int, assetType: AssetReference.Type): List<AssetReference> {
    return client.get("$VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX") {
        parameter("assetType", assetType)
    }
}

/**
 * List all images from image service.
 *
 * @param client HTTP client.
 */
suspend fun listImages(client: HttpClient): List<Image> {
    return client.get(IMAGE_ENDPOINT)
}

/**
 * List images, filtered by container ID, from image service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 */
suspend fun listImages(client: HttpClient, containerId: Int): List<Image> {
    return client.get(IMAGE_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List all videos from video service.
 *
 * @param client HTTP client.
 */
suspend fun listVideos(client: HttpClient): List<Video> {
    return client.get(VIDEO_ENDPOINT)
}

/**
 * List videos, filtered by container ID, from video service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 */
suspend fun listVideos(client: HttpClient, containerId: Int): List<Video> {
    return client.get(VIDEO_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List videos, filtered by video type, from video service.
 *
 * @param client HTTP client.
 * @param type Video type to filter by.
 */
suspend fun listVideos(client: HttpClient, type: Video.Type): List<Video> {
    return client.get(VIDEO_ENDPOINT) {
        parameter("type", type.name)
    }
}

/**
 * List videos, filtered by container ID and video type, from video service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 * @param type Video type to filter by.
 */
suspend fun listVideos(client: HttpClient, containerId: Int, type: Video.Type): List<Video> {
    return client.get(VIDEO_ENDPOINT) {
        parameter("containerId", containerId)
        parameter("type", type.name)
    }
}
