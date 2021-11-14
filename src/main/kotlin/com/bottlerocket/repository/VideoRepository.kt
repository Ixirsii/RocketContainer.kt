package com.bottlerocket.repository

import com.bottlerocket.Logging
import com.bottlerocket.LoggingImpl
import com.bottlerocket.data.AssetType
import com.bottlerocket.data.VideoType
import com.bottlerocket.data.videoService.Video
import com.bottlerocket.data.videoService.VideoAssets
import com.bottlerocket.data.videoService.Videos
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.parameter
import kotlinx.coroutines.runBlocking

/**
 * Video service repository.
 */
class VideoRepository(client: HttpClient) : Logging by LoggingImpl<VideoRepository>(), Repository(client) {
    /** Asset reference endpoint is in the format $VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX. */
    private val assetReferenceSuffix = "asset-references"

    /** URL endpoint for video service. */
    private val endpoint = "http://videos.rocket-stream.bottlerocketservices.com/videos"

    /**
     * Get video by ID from video service.
     *
     * @param videoId ID of the video to get.
     * @return requested video.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun getVideo(videoId: Int): Video = runBlocking {
        log.debug("Getting video {}", videoId)
        exponentialBackoffAndRetry("$endpoint/$videoId")
    }

    /**
     * List all asset references for a video from video service.
     *
     * @param videoId ID of the video to get assets from.
     * @return all asset references for specified video.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun listAssetReferences(videoId: Int): VideoAssets = runBlocking {
        log.debug("Listing asset references for video {}", videoId)
        exponentialBackoffAndRetry("$endpoint/$videoId/$assetReferenceSuffix")
    }

    /**
     * List asset references, filtered by asset type, from video service.
     *
     * @param videoId ID of the video to get assets from.
     * @param assetType Asset type to filter by.
     * @return filtered asset references for specified video.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun listAssetReferences(videoId: Int, assetType: AssetType): VideoAssets = runBlocking {
        log.debug("Listing asset references for video {} by type {}", videoId, assetType)
        exponentialBackoffAndRetry("$endpoint/$videoId/$assetReferenceSuffix") {
            parameter("assetType", assetType)
        }
    }

    /**
     * List all videos from video service.
     *
     * @return all videos.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun listVideos(): Videos = runBlocking {
        log.debug("Listing videos")
        exponentialBackoffAndRetry(endpoint)
    }

    /**
     * List videos, filtered by container ID, from video service.
     *
     * @param containerId Container ID to filter by.
     * @return filtered list of videos.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun listVideos(containerId: Int): Videos = runBlocking {
        log.debug("Listing videos by container {}", containerId)
        exponentialBackoffAndRetry(endpoint) {
            parameter("containerId", containerId)
        }
    }

    /**
     * List videos, filtered by video type, from video service.
     *
     * @param type Video type to filter by.
     * @return filtered list of videos.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun listVideos(type: VideoType): Videos = runBlocking {
        log.debug("Listing videos by type {}", type)
        exponentialBackoffAndRetry(endpoint) {
            parameter("type", type.name)
        }
    }

    /**
     * List videos, filtered by container ID and video type, from video service.
     *
     * @param containerId Container ID to filter by.
     * @param type Video type to filter by.
     * @return filtered list of videos.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun listVideos(containerId: Int, type: VideoType): Videos = runBlocking {
        log.debug("Listing videos by container {} and type {}", containerId, type)
        exponentialBackoffAndRetry(endpoint) {
            parameter("containerId", containerId)
            parameter("type", type.name)
        }
    }
}
