package com.bottlerocket.repository

import com.bottlerocket.Logging
import com.bottlerocket.LoggingImpl
import com.bottlerocket.data.advertisementService.Advertisements
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.parameter
import kotlinx.coroutines.runBlocking

/**
 * Advertisement service repository.
 */
class AdvertisementRepository(client: HttpClient) :
        Logging by LoggingImpl<AdvertisementRepository>(),
        Repository(client) {
    /** URL endpoint for advertisement service. */
    private val endpoint = "http://ads.rocket-stream.bottlerocketservices.com/advertisements"

    /**
     * List all advertisements from advertisement service.
     *
     * @return all advertisements.
     * @throws ResponseException if the maximum number of attempts is exceeded.
     */
    fun listAdvertisements(): Advertisements = runBlocking {
        log.debug("Listing advertisements")
        exponentialBackoffAndRetry(endpoint)
    }

    /**
     * List advertisements, filtered by container ID, from advertisement service.
     *
     * @param containerId Container ID to filter by.
     * @return filtered list of advertisements.
     * @throws ResponseException if the maximum number of attempts is exceeded.
     */
    fun listAdvertisements(containerId: Int): Advertisements = runBlocking {
        log.debug("Listing advertisements by container {}", containerId)
        exponentialBackoffAndRetry(endpoint) {
            parameter("containerId", containerId)
        }
    }
}
