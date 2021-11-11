package com.bottlerocket.repository

import com.bottlerocket.data.AssetType
import com.bottlerocket.data.VideoType
import com.bottlerocket.data.advertisementService.Advertisements
import com.bottlerocket.data.imageService.Images
import com.bottlerocket.data.videoService.Video
import com.bottlerocket.data.videoService.VideoAssets
import com.bottlerocket.data.videoService.Videos
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

val log: Logger = LoggerFactory.getLogger("Repository")

/**
 * Get video by ID from video service.
 *
 * @param client HTTP client.
 * @param videoId ID of the video to get.
 * @return requested video.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun getVideo(client: HttpClient, videoId: Int): Video = runBlocking {
    log.debug("Getting video {}", videoId)
    get(client, "$VIDEO_ENDPOINT/$videoId")
}

/**
 * List all advertisements from advertisement service.
 *
 * @param client HTTP client.
 * @return all advertisements.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listAdvertisements(client: HttpClient): Advertisements = runBlocking {
    log.debug("Listing advertisements")
    get(client, ADVERTISEMENT_ENDPOINT)
}

/**
 * List advertisements, filtered by container ID, from advertisement service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 * @return filtered list of advertisements.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listAdvertisements(client: HttpClient, containerId: Int): Advertisements = runBlocking {
    log.debug("Listing advertisements by container {}", containerId)
    get(client, ADVERTISEMENT_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List all asset references for a video from video service.
 *
 * @param client HTTP client.
 * @param videoId ID of the video to get assets from.
 * @return all asset references for specified video.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listAssetReferences(client: HttpClient, videoId: Int): VideoAssets = runBlocking {
    log.debug("Listing asset references for video {}", videoId)
    get(client, "$VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX")
}

/**
 * List asset references, filtered by asset type, from video service.
 *
 * @param client HTTP client.
 * @param videoId ID of the video to get assets from.
 * @param assetType Asset type to filter by.
 * @return filtered asset references for specified video.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listAssetReferences(client: HttpClient, videoId: Int, assetType: AssetType): VideoAssets = runBlocking {
    log.debug("Listing asset references for video {} by type {}", videoId, assetType)
    get(client, "$VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX") {
        parameter("assetType", assetType)
    }
}

/**
 * List all images from image service.
 *
 * @param client HTTP client.
 * @return all images.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listImages(client: HttpClient): Images = runBlocking {
    log.debug("Listing images")
    get(client, IMAGE_ENDPOINT)
}

/**
 * List images, filtered by container ID, from image service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 * @return filtered list of images.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listImages(client: HttpClient, containerId: Int): Images = runBlocking {
    log.debug("Listing images by container {}", containerId)
    get(client, IMAGE_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List all videos from video service.
 *
 * @param client HTTP client.
 * @return all videos.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listVideos(client: HttpClient): Videos = runBlocking {
    log.debug("Listing videos")
    get(client, VIDEO_ENDPOINT)
}

/**
 * List videos, filtered by container ID, from video service.
 *
 * @param client HTTP client.
 * @param containerId Container ID to filter by.
 * @return filtered list of videos.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listVideos(client: HttpClient, containerId: Int): Videos = runBlocking {
    log.debug("Listing videos by container {}", containerId)
    get(client, VIDEO_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

/**
 * List videos, filtered by video type, from video service.
 *
 * @param client HTTP client.
 * @param type Video type to filter by.
 * @return filtered list of videos.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listVideos(client: HttpClient, type: VideoType): Videos = runBlocking {
    log.debug("Listing videos by type {}", type)
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
 * @return filtered list of videos.
 * @throws ResponseException if the maximum number of attempts is exceeded.
 */
fun listVideos(client: HttpClient, containerId: Int, type: VideoType): Videos = runBlocking {
    log.debug("Listing videos by container {} and type {}", containerId, type)
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
 * @throws ResponseException if the maximum number of attempts is exceeded.
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
            val backoff: Long = exponentialBackoff(i)
            log.trace("Caught ResponseException calling {} on attempt {}. Waiting {}ms", url, i, backoff, e)
            delay(backoff)
        }
    }

    return client.get(url, block)
}
