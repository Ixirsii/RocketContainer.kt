package com.bottlerocket.repository

import com.bottlerocket.Logging
import com.bottlerocket.LoggingImpl
import com.bottlerocket.data.imageService.Images
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.parameter
import kotlinx.coroutines.runBlocking

/**
 * Image service repository.
 */
class ImageRepository(client: HttpClient) : Logging by LoggingImpl<ImageRepository>(), Repository(client) {
    /** URL endpoint for image service. */
    private val endpoint = "http://images.rocket-stream.bottlerocketservices.com/images"

    /**
     * List all images from image service.
     *
     * @return all images.
     * @throws ResponseException if the maximum number of attempts is exceeded.
     */
    fun listImages(): Images = runBlocking {
        log.debug("Listing images")
        exponentialBackoffAndRetry(endpoint)
    }

    /**
     * List images, filtered by container ID, from image service.
     *
     * @param containerId Container ID to filter by.
     * @return filtered list of images.
     * @throws ResponseException if the maximum number of attempts is exceeded.
     */
    fun listImages(containerId: Int): Images = runBlocking {
        log.debug("Listing images by container {}", containerId)
        exponentialBackoffAndRetry(endpoint) {
            parameter("containerId", containerId)
        }
    }
}
