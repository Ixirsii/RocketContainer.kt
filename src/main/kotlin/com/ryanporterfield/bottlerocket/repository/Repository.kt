package com.ryanporterfield.bottlerocket.repository

import com.ryanporterfield.bottlerocket.data.AssetType
import com.ryanporterfield.bottlerocket.data.VideoType
import com.ryanporterfield.bottlerocket.data.advertisementService.Advertisements
import com.ryanporterfield.bottlerocket.data.imageService.Images
import com.ryanporterfield.bottlerocket.data.videoService.Video
import com.ryanporterfield.bottlerocket.data.videoService.VideoAssets
import com.ryanporterfield.bottlerocket.data.videoService.Videos
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

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
fun getVideo(client: HttpClient, videoId: Int): Video = runBlocking {
    get(client, "$VIDEO_ENDPOINT/$videoId")
}

/**
 * List all advertisements from advertisement service.
 *
 * @param client HTTP client.
 */
fun listAdvertisements(client: HttpClient): Advertisements = runBlocking {
    get(client, ADVERTISEMENT_ENDPOINT)
}

/**
 * List advertisements, filtered by container ID, from advertisement service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 */
fun listAdvertisements(client: HttpClient, containerId: Int): Advertisements = runBlocking {
    get(client, ADVERTISEMENT_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List all asset references for a video from video service.
 *
 * @param client HTTP client.
 * @param videoId ID of the video to get assets from.
 */
fun listAssetReferences(client: HttpClient, videoId: Int): VideoAssets = runBlocking {
    get(client, "$VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX")
}

/**
 * List asset references, filtered by asset type, from video service.
 *
 * @param client HTTP client.
 * @param videoId ID of the video to get assets from.
 * @param assetType Asset type to filter by.
 */
fun listAssetReferences(client: HttpClient, videoId: Int, assetType: AssetType): VideoAssets = runBlocking {
    get(client, "$VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX") {
        parameter("assetType", assetType)
    }
}

/**
 * List all images from image service.
 *
 * @param client HTTP client.
 */
fun listImages(client: HttpClient): Images = runBlocking {
    get(client, IMAGE_ENDPOINT)
}

/**
 * List images, filtered by container ID, from image service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 */
fun listImages(client: HttpClient, containerId: Int): Images = runBlocking {
    get(client, IMAGE_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List all videos from video service.
 *
 * @param client HTTP client.
 */
fun listVideos(client: HttpClient): Videos = runBlocking {
    get(client, VIDEO_ENDPOINT)
}

/**
 * List videos, filtered by container ID, from video service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 */
fun listVideos(client: HttpClient, containerId: Int): Videos = runBlocking {
    get(client, VIDEO_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List videos, filtered by video type, from video service.
 *
 * @param client HTTP client.
 * @param type Video type to filter by.
 */
fun listVideos(client: HttpClient, type: VideoType): Videos = runBlocking {
    get(client, VIDEO_ENDPOINT) {
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
fun listVideos(client: HttpClient, containerId: Int, type: VideoType): Videos = runBlocking {
    get(client, VIDEO_ENDPOINT) {
        parameter("containerId", containerId)
        parameter("type", type.name)
    }
}

/* ****************************************************************************************************************** *
 *                                             Private utility functions                                              *
 * ****************************************************************************************************************** */

/**
 * Get exponential backoff delay.
 *
 * @param attempt Attempt number.
 * @return exponential backoff delay.
 */
private fun exponentialBackoff(attempt: Int): Long {
    val maxBackoff = 3_000L
    val exponent: Long = 2f.pow(attempt).toLong()
    val randomMillis: Long = Random.nextLong(0, 1_000)
    val backoff: Long = exponent + randomMillis

    return min(backoff, maxBackoff)
}

/**
 * Make a get request with retries and exponential backoff.
 *
 * @param client HTTP client.
 * @param url Request URL.
 * @param block [HttpRequestBuilder] used to configure the request.
 * @return deserialized data from endpoint.
 */
private suspend inline fun <reified T> get(
    client: HttpClient,
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    val maxAttempts = 3

    // Start at 1 instead of 0 because the last attempt is outside the loop
    for (i in (1 until maxAttempts)) {
        try {
            return client.get(url, block)
        } catch (e: ResponseException) {
            // TODO: Log exception and delay
            val backoff: Long = exponentialBackoff(i)
            delay(backoff)
        }
    }

    return client.get(url, block)
}
