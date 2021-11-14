package com.bottlerocket.repository

import com.bottlerocket.Logging
import com.bottlerocket.LoggingImpl
import com.bottlerocket.data.advertisementService.Advertisements
import com.bottlerocket.data.containerService.Advertisement
import com.bottlerocket.data.containerService.AssetReference
import com.bottlerocket.data.containerService.Container
import com.bottlerocket.data.containerService.Image
import com.bottlerocket.data.containerService.Video
import com.bottlerocket.data.imageService.Images
import com.bottlerocket.service.convertAdvertisement
import com.bottlerocket.service.convertAssetReference
import com.bottlerocket.service.convertContainer
import com.bottlerocket.service.convertImage
import com.bottlerocket.service.convertVideo
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException

/**
 * Wrapper for dependency repositories which builds [Container]s.
 */
class ContainerRepository(
    private val advertisementRepository: AdvertisementRepository,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
) : Logging by LoggingImpl<ContainerRepository>() {

    /**
     * Get a container by ID.
     *
     * @param containerId Container ID.
     * @return container by ID.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     * @throws IllegalArgumentException if there are no videos for the provided container ID.
     */
    @Throws(
        RedirectResponseException::class,
        ClientRequestException::class,
        ServerResponseException::class,
        IllegalArgumentException::class
    )
    fun getContainer(containerId: Int): Container {
        log.debug("Getting container {}", containerId)

        val videos: List<Video> = videoRepository.listVideos(containerId).videos
            .map { video ->
                val assets: List<AssetReference> = videoRepository.listAssetReferences(video.id).videoAssets
                    .map { convertAssetReference(it) }
                convertVideo(video, assets)
            }

        if (videos.isEmpty()) {
            throw IllegalArgumentException("Invalid container ID")
        }

        val advertisements: List<Advertisement> = advertisementRepository.listAdvertisements(containerId).advertisements
            .map { convertAdvertisement(it) }
        val images: List<Image> = imageRepository.listImages(containerId).images
            .map { convertImage(it) }

        return convertContainer(containerId, advertisements, images, videos)
    }

    /**
     * Get a list of all containers.
     *
     * @return all containers.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    fun listContainers(): List<Container> {
        log.debug("Listing containers")

        val videos: Map<Int, List<Video>> = getVideos()
        val advertisements: Advertisements = advertisementRepository.listAdvertisements()
        val images: Images = imageRepository.listImages()

        return videos.map { entry ->
            val ads: List<Advertisement> = advertisements.advertisements
                .filter { it.containerId == entry.key }
                .map { convertAdvertisement(it) }
            val containerImages: List<Image> = images.images
                .filter { it.containerId == entry.key }
                .map { convertImage(it) }

            convertContainer(entry.key, ads, containerImages, entry.value)
        }
    }

    /* ********************************************************************************************************** *
     *                                             Private utility functions                                      *
     * ********************************************************************************************************** */

    /**
     * Get a map of container ID to a list of videos in that container.
     *
     * @return map of container ID to a list of videos.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    private fun getVideos(): Map<Int, List<Video>> {
        val videos: MutableMap<Int, MutableList<Video>> = HashMap()

        videoRepository.listVideos().videos.parallelStream()
            .map { video ->
                val assets: List<AssetReference> = videoRepository.listAssetReferences(video.id).videoAssets
                    .map { convertAssetReference(it) }

                Pair(video.containerId, convertVideo(video, assets))
            }
            .forEach { pair ->
                if (videos[pair.first]?.add(pair.second) != true) {
                    videos[pair.first] = mutableListOf(pair.second)
                }
            }

        return videos
    }
}
