package com.bottlerocket.service

import com.bottlerocket.data.advertisementService.Advertisements
import com.bottlerocket.data.containerService.Advertisement
import com.bottlerocket.data.containerService.AssetReference
import com.bottlerocket.data.containerService.Container
import com.bottlerocket.data.containerService.Image
import com.bottlerocket.data.containerService.Video
import com.bottlerocket.data.imageService.Images
import com.bottlerocket.repository.listAdvertisements
import com.bottlerocket.repository.listAssetReferences
import com.bottlerocket.repository.listImages
import com.bottlerocket.repository.listVideos
import io.ktor.client.HttpClient

/** TODO: Refactor service & repository layers to be more easily testable.
 * Get a list of all containers.
 *
 * @param client Http client.
 * @return all containers.
 */
fun listContainers(client: HttpClient): List<Container> {
    val advertisements: Advertisements = listAdvertisements(client)
    val images: Images = listImages(client)
    val videos: Map<Int, List<Video>> = getVideos(client)

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
 * Get a map of container ID to a list of videos in that container.
 *
 * @param client Http client.
 * @return map of container ID to a list of videos.
 */
private fun getVideos(client: HttpClient): Map<Int, List<Video>> {
    val videos: MutableMap<Int, MutableList<Video>> = HashMap()

    for (video in listVideos(client).videos) {
        val assets: List<AssetReference> = listAssetReferences(client, video.id).videoAssets
            .map { convertAssetReference(it) }

        if (videos[video.containerId]?.add(convertVideo(video, assets)) != true) {
            videos[video.containerId] = mutableListOf(convertVideo(video, assets))
        }
    }

    return videos
}
