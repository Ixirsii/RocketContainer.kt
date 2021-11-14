package com.bottlerocket.service

import com.bottlerocket.data.containerService.Advertisement
import com.bottlerocket.data.containerService.Container
import com.bottlerocket.data.containerService.Image
import com.bottlerocket.data.containerService.Video
import com.bottlerocket.repository.ContainerRepository
import io.github.pavleprica.kotlin.cache.time.based.CustomTimeBasedCache
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import java.time.Duration
import java.util.Optional

/**
 * Service/business logic layer for containers.
 */
class ContainerService(
    /** Container repository for getting data from dependencies. */
    private val containerRepository: ContainerRepository,
    /** Data cache. */
    private val cache: CustomTimeBasedCache<Int, Container> = CustomTimeBasedCache(Duration.ofMinutes(15))
) {

    /**
     * List all containers.
     *
     * @return all containers.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun listContainers(): List<Container> {
        val containers: List<Container> = containerRepository.listContainers()

        containers.forEach { cache[it.id] = it }

        return containers
    }

    /**
     * Get advertisements for a container.
     *
     * @param containerId Container ID.
     * @return container advertisements.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun getAdvertisements(containerId: Int): List<Advertisement> {
        val container: Container = getContainer(containerId)

        return container.ads
    }

    /**
     * Get a container by ID.
     *
     * @param containerId Container ID.
     * @return container by ID.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun getContainer(containerId: Int): Container {
        val cacheValue: Optional<Container> = cache[containerId]

        if (cacheValue.isPresent) {
            return cacheValue.get()
        }

        val container: Container = containerRepository.getContainer(containerId)

        cache[containerId] = container

        return container
    }

    /**
     * Get images for a container.
     *
     * @param containerId Container ID.
     * @return container images.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun getImages(containerId: Int): List<Image> {
        val container: Container = getContainer(containerId)

        return container.images
    }

    /**
     * Get videos for a container.
     *
     * @param containerId Container ID.
     * @return container videos.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun getVideos(containerId: Int): List<Video> {
        val container: Container = getContainer(containerId)

        return container.videos
    }
}
