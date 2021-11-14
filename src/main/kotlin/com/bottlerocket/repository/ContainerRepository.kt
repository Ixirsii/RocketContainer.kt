package com.bottlerocket.repository

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

/**
 * Wrapper for dependency repositories which builds [Container]s.
 */
class ContainerRepository(
    private val advertisementRepository: AdvertisementRepository,
    private val imageRepository: ImageRepository,
    private val videoRepository: VideoRepository
) {
    /**
     * Get a list of all containers.
     *
     * @return all containers.
     */
    fun listContainers(): List<Container> {
        val advertisements: Advertisements = advertisementRepository.listAdvertisements()
        val images: Images = imageRepository.listImages()
        val videos: Map<Int, List<Video>> = getVideos()

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

    /**
     * Get a container by ID.
     *
     * @param containerId Container ID.
     * @return container by ID.
     */
    fun getContainer(containerId: Int): Container {
        val advertisements: List<Advertisement> = advertisementRepository.listAdvertisements(containerId).advertisements
            .map { convertAdvertisement(it) }
        val images: List<Image> = imageRepository.listImages(containerId).images
            .map { convertImage(it) }
        val videos: List<Video> = videoRepository.listVideos(containerId).videos
            .map { video ->
                val assets: List<AssetReference> = videoRepository.listAssetReferences(video.id).videoAssets
                    .map { convertAssetReference(it) }
                convertVideo(video, assets)
            }

        return convertContainer(containerId, advertisements, images, videos)
    }

    /* ********************************************************************************************************** *
     *                                             Private utility functions                                      *
     * ********************************************************************************************************** */

    /**
     * Get a map of container ID to a list of videos in that container.
     *
     * @return map of container ID to a list of videos.
     */
    private fun getVideos(): Map<Int, List<Video>> {
        val videos: MutableMap<Int, MutableList<Video>> = HashMap()

        for (video in videoRepository.listVideos().videos) {
            val assets: List<AssetReference> = videoRepository.listAssetReferences(video.id).videoAssets
                .map { convertAssetReference(it) }

            if (videos[video.containerId]?.add(convertVideo(video, assets)) != true) {
                videos[video.containerId] = mutableListOf(convertVideo(video, assets))
            }
        }

        return videos
    }
}
